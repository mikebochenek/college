/*
 * MICHAEL BOCHENEK (CS1790) ID 0041056
 * ASSIGNMENT #3 FOR CIS*3110
 *
 *********************************************************
 * This program implements a very simple file system, emulated on a single
 * Unix disk file which can be the floppy device /dev/fd0, if you wish.
 *
 * The main program simply tests the system, which is implemented as the
 * set of operation routines. For a production system, these would be used
 * by a generic file system switch to access files on this specific file
 * system.
 * Compile with "cc diskio_demo_lite.c" or
 * "cc -DDEBUG diskio_demo_lite.c" if you
 * want debugging output.
 * To run the program:
 * "a.out <disk_file> <control_file>", for example:
 * % a.out xxx.dat diskio_demo.dat1
 * OR
 * % a.out /dev/fd0 diskio_demo.dat1
 * NB: You must have a write enabled scratch floppy in the drive, if you
 *     use /dev/fd0.
 * See the comment inside main() for more details on the control file used
 * for testing. Note that diskio_demo.dat2 does not format the file system
 * and assumes that diskio_demo.dat1 has already been run to prepare the
 * file system.
 */
#include <stdio.h>
#include <fcntl.h>

// -------------------------------------------

/*
 * Heads of the lists. The LRU list is maintained so that the Least Recently
 * Used can be replaced. The Hash lists are used to speed up searches.
 */
#define	HASH_SIZE	(6)
#define	HASH_CODE(c)	((c) % HASH_SIZE)
//#define	HASH_CODE(c)	((*((char *)(c))) % HASH_SIZE)
#define	FS_BLKSIZE	512
#define	FS_DATASIZE	1000
#define	FS_NAMELEN	14
#define	FS_INODEBLOCK	1
#define	FS_FREEBLOCK	2
#define	FS_DATABLOCK0	3
#define	FIRST_BLKS	3	/* How many blk numbers are in inode */
#define	INDIRECT_SIZE	((int)(FS_BLKSIZE / sizeof (int)))
#define	LITE_OUTOFSPACE	-1
#define	INODE_SIZE	((int)(FS_BLKSIZE / sizeof (struct inode)))
#define	FS_ROOTINODE	0
#define	FS_CACHESIZE	5


struct buf_list {
	struct	buf_list	*lru_next;	/* LRU List */
	struct	buf_list	*lru_prev;
	struct	buf_list	*hash_next;	/* Hash List */
	struct	buf_list	*hash_prev;
	int	file_blk;		/* File block # */
	int	file_pos;		/* File position in directory */
	int	dirty;			/* Has block been modified? */
	char	file_data[FS_BLKSIZE];	/* File block itself */
   int			data_size;	/* Data size */
	int			alloc_size;	/* Size structure is alloc'd */
	char			data[1];	/* Actual size is malloc'd */
};
#define	NO_VALID_DATA	(-1)
struct buf_list hash_table[FS_CACHESIZE];
struct buf_list lru_head;
int counter [FS_CACHESIZE];


/*
 * Macros that handle the linked lists.
 */
#define	INSERT_LRU(p, l) \
	(l)->lru_next->lru_prev = (p); \
	(p)->lru_prev = (l); \
	(p)->lru_next = (l)->lru_next; \
	(l)->lru_next = (p)
#define	INSERT_HASH(p, l) \
	(l)->hash_next->hash_prev = (p); \
	(p)->hash_prev = (l); \
	(p)->hash_next = (l)->hash_next; \
	(l)->hash_next = (p)
#define	REMOVE_LRU(p) \
	(p)->lru_prev->lru_next = (p)->lru_next; \
	(p)->lru_next->lru_prev = (p)->lru_prev
#define	REMOVE_HASH(p) \
	(p)->hash_prev->hash_next = (p)->hash_next; \
	(p)->hash_next->hash_prev = (p)->hash_prev

/*
 * The number of units allocated and limits on allocation.
 * (Make the limits small for our artificial test.)
 */
int list_cnt;
#define	LOW_WATER_MARK	3
#define	HIGH_WATER_MARK	6

void		add_data();
struct buf_list *search_data();
int		delete_data();

/*
 * Add data to the lists. If there is a free element on the LRU list,
 * use it. Otherwise, allocate a new element unless the HIGH_WATER_MARK
 * has been reached. In that case, replace the Least Recently Used element.
 * Link it to the head of the LRU list and into the correct hash list.
 */
void
add_data(file_blk, file_pos, dirty, file_data)
   int file_blk;
   int file_pos;
   int dirty;	
   char *file_data;
{
	register struct buf_list *tp;

	if (lru_head.lru_prev->file_pos == NO_VALID_DATA) {
		tp = lru_head.lru_prev;
		REMOVE_LRU(tp);
	} else if (list_cnt < HIGH_WATER_MARK) {
		tp = (struct buf_list *) malloc(sizeof (struct buf_list));
		list_cnt++;
	} else {
		tp = lru_head.lru_prev;
		REMOVE_LRU(tp);
		REMOVE_HASH(tp);
	}

	/*
	 * Fill in the data and add it to the lists.
	 */
	bcopy((char *)file_data, tp->file_data, FS_BLKSIZE);
	tp->file_pos = file_pos;
   tp->file_blk = file_blk;
   tp->dirty = dirty;
	INSERT_LRU(tp, &lru_head);
	INSERT_HASH(tp, &hash_table[HASH_CODE(tp->file_blk)]);
}

/*
 * Search for a data match in the list.
 */
struct buf_list *
search_data(file_blk)
	int file_blk;
{
	register struct buf_list *tp;
	register int hash_index;

	hash_index = HASH_CODE(file_blk);
	tp = hash_table[hash_index].hash_next;
	while (tp != &hash_table[hash_index]) {
		if ((tp->file_blk == file_blk)) {
		//if (!(tp->file_blk == file_blk)) {
			REMOVE_LRU(tp);
			INSERT_LRU(tp, &lru_head);
			return (tp);
		}
		tp = tp->hash_next;
	}
	return ((struct buf_list *)0);
}

/*
 * Delete data element. If the allocation is above the low water mark,
 * free the entry, otherwise just leave it on the tail of the LRU list.
 */
int
delete_data(file_blk)
	int file_blk;
{
	register struct buf_list *tp;

	tp = search_data(file_blk);
	if (tp) {
		REMOVE_LRU(tp);
		REMOVE_HASH(tp);
		if (list_cnt > LOW_WATER_MARK) {
			free((char *)tp);
			list_cnt--;
		} else {
			tp->file_pos = NO_VALID_DATA;
			INSERT_LRU(tp, lru_head.lru_prev);
		}
		return (1);
	}
	return (0);
}
// -------------------------------------------

/*
 * Here are the routines and global data structures that implement the
 * simple file system. Everything from here down to main() can be thought
 * of as a library.
 */

/*
 * Structure of an i-node. An i-node is the information block for each file.
 */
struct inode {
	int	file_size;	/* File size in bytes			*/
	int	modify_time;	/* Time file last modified		*/
	short	owner;		/* User id of owner of file		*/
	short	type;		/* Type of i-node:I_FREE,I_REGULAR,I_DIR */
	int	protection;	/* Protection Bits			*/
	int	first_blocks[FIRST_BLKS];/* First FIRST_BLKS data blocks*/
	int	indirect_blk;	/* Number of the indirect block		*/
};

struct indirect_blk {
	int	indirect_blkptr[INDIRECT_SIZE];	/* Pointers to more blocks */
};

/*
 * This structure defines a directory entry for the file system.
 * (Directory entries are found in files of inode type I_DIR.)
 */
struct fsdir_entry {
	char	inode_num;		/* Number of associated inode */
	char	file_name[FS_NAMELEN + 1]; /* File name, null terminated */
};


/*
 * Types of inodes:
 *	Free - unallocated
 *	Regular - Ordinary file
 *	Dir - directory
 */
#define	I_FREE		1
#define	I_REGULAR	2
#define	I_DIR		3

int current_dir = FS_ROOTINODE;		/* Current directory i-node */
char *get_buffer();
void dismount_fs(), mark_modified(), mark_invalid();

/*
 * Global data for the file system. The main data structures are the
 * in memory copy of the i-node table , free list and the buffer cache.
 */
int	mnt_valid;		/* Is a file system mounted? */
int	mnt_device;		/* File/dev that is mounted */
struct inode	mnt_inode[INODE_SIZE]; /* Inode block */
int	mnt_inode_dirty;	/* Has inode block been modified? */
struct indirect_blk mnt_free;	/* Free block */
int	mnt_free_dirty;		/* Has the free block been modified? */
int   directory_stack[64];
int   stack_location = 0; 

/*
 * This structure defines an entry in the logical (file) block buffer
 * cache.
 */

struct buf_entry {
	int	file_blk;		/* File block # */
   int	file_pos;		/* File position in directory */
	int	dirty;			/* Has block been modified? */
	char	file_data[FS_BLKSIZE];	/* File block itself */
};

//struct buf_entry buf_cache[FS_CACHESIZE]; /* The buffer cache */

/*
 * Create the file system structure. (Often referred to as a "high level
 * format".)
 *
 * The file descriptor argument "device" is the open device.
 * Returns 0 for success and -1 for failure.
 */
int
format_fs(device)
	int device;
{
	static struct inode fs_inode[INODE_SIZE];
	static struct indirect_blk fs_free;
	register int i;

	/*
	 * Initialize the inode block, free block and root directory.
	 * First, mark all entries free.
	 */
	for (i = 0; i < INODE_SIZE; i++)
		fs_inode[i].type = I_FREE;
	for (i = 0; i < INDIRECT_SIZE; i++)
		fs_free.indirect_blkptr[i] = i;

	/*
	 * Now, handcraft the root directory.
	 */
	fs_inode[FS_ROOTINODE].file_size = 0;
	fs_inode[FS_ROOTINODE].modify_time = time(0);
	fs_inode[FS_ROOTINODE].owner = 0;
	fs_inode[FS_ROOTINODE].type = I_DIR;
	fs_inode[FS_ROOTINODE].protection = 0777;
	for (i = 0; i < FIRST_BLKS; i++)
		fs_inode[FS_ROOTINODE].first_blocks[i] = -1;
	fs_inode[FS_ROOTINODE].indirect_blk = -1;

	/*
	 * Now, write the inode table and free list out to disk.
	 */
	lseek(device, (off_t)(FS_INODEBLOCK * FS_BLKSIZE), SEEK_SET);
	if (write(device, fs_inode, FS_BLKSIZE) != FS_BLKSIZE)
		return (-1);
	if (write(device, &fs_free, FS_BLKSIZE) != FS_BLKSIZE)
		return (-1);
	return (0);
}

/*
 * Mount the file system.
 * Basically, read the i-node table and free list into memory.
 *
 * The argument "device" is the open file descriptor for the disk.
 * Returns 0 for success and -1 for failure.
 */
int
mount_fs(device)
	int device;
{
	register int i;
	/*
	 * First, read the i-node table into memory.
	 */
	lseek(device, (off_t)(FS_INODEBLOCK * FS_BLKSIZE), SEEK_SET);
	if (read(device, mnt_inode, FS_BLKSIZE) != FS_BLKSIZE)
		return (-1);
	if (read(device, &mnt_free, FS_BLKSIZE) != FS_BLKSIZE)
		return (-1);

	/*
	 * Now, do some sanity checks on the i-node and freelist to see if
	 * they look ok.
	 */
	for (i = 0; i < INODE_SIZE; i++)
		if (mnt_inode[i].type != I_FREE &&
		    mnt_inode[i].type != I_REGULAR &&
		    mnt_inode[i].type != I_DIR)
			return (-1);
	for (i = 0; i < INDIRECT_SIZE; i++)
		if (mnt_free.indirect_blkptr[i] < -1 ||
		    mnt_free.indirect_blkptr[i] >= FS_DATASIZE)
			return (-1);

	/*
	 * Now, just finish off initializing the data structures.
	 */
	mnt_valid = 1;
	mnt_device = device;
	mnt_inode_dirty = 0;
	mnt_free_dirty = 0;
	
	/* no longer necessary to init buffer...
	for (i = 0; i < FS_CACHESIZE; i++)
		buf_cache[i].file_pos = -1;
   */
	
   for (i = 0; i < FS_CACHESIZE; i++)
   {
      hash_table[i].file_blk = -1;
      hash_table[i].file_pos = -1;
      hash_table[i].dirty = 0;
      hash_table[i].lru_next = NULL;
      hash_table[i].lru_prev = NULL;
   }	

	lru_head.lru_next = lru_head.lru_prev = &lru_head;

   /*
      initiated since it's a linked list
   temp.file_pos = -1;
	temp.dirty = 0;
	add_data (&temp, sizeof(temp));
   */	

	return (0);
}

/*
 * Dismount the file system. Basically flush everything to disk and
 * then mark the mount invalid.
 */
void
dismount_fs()
{
	register int i;
	struct buf_list *iterator;
   iterator = &lru_head;

   for (i = 0; i < FS_CACHESIZE; i++)
   {
      if (hash_table[i].dirty && hash_table[i].file_pos >= 0)
      {
	      if (write_block (hash_table[i].file_pos, hash_table[i].file_data, hash_table[i].file_blk) < 0)
         {
			   fprintf(stderr, "Yuck! disk out of space!\n");
		   }
      }
   }

	if (mnt_inode_dirty) {
		lseek(mnt_device,(off_t)(FS_INODEBLOCK * FS_BLKSIZE),SEEK_SET);
		if (write(mnt_device, mnt_inode, FS_BLKSIZE) < 0)
			fprintf(stderr, "Panic: Can't write inode table\n");
	}
	if (mnt_free_dirty) {
		lseek(mnt_device, (off_t)(FS_FREEBLOCK * FS_BLKSIZE), SEEK_SET);
		if (write(mnt_device, &mnt_free, FS_BLKSIZE) < 0)
			fprintf(stderr, "Panic: Can't write free list\n");
	}
	mnt_valid = 0;
}

/*
 * Create a new file.
 * Basically search the directory for a free element and allocate it,
 * as required. (If the file already exists, it simply returns the
 * directory index for it.)
 *
 * The argument "new_name" is the file name string, which cannot exceed
 * FS_NAMELEN non-null bytes.
 * Returns the index in the directory for success, -1 for failure.
 */
int
create_file(new_name, type)
	char *new_name;
	short type;
{
	register int i, j, offset;
	struct fsdir_entry dir;

	/*
	 * First, perform sanity checks.
	 */
	if (mnt_valid == 0 || strlen(new_name) > FS_NAMELEN)
		return (-1);

	/*
	 * Check to see if the file already exists. If so, just return
	 * its position.
	 */
	if ((i = lookup_file(new_name)) >= 0)
	{
		return (i);
	}

	/*
	 * Now, search for a free i-node.
	 */
	for (i = 0; i < INODE_SIZE; i++)
		if (mnt_inode[i].type == I_FREE)
			break;
	if (i == INODE_SIZE)
		return (-1);

	/*
	 * Set up the new directory entry for the file with a length of 0.
	 * First, allocate an i-node and then create a directory entry.
	 */
#ifdef DEBUG
	fprintf(stderr, "Setting up new i-node for %s at pos %d\n",
		new_name, i);
#endif
	mnt_inode[i].file_size = 0;
	mnt_inode[i].modify_time = time(0);
	mnt_inode[i].owner = 0;
	mnt_inode[i].type = type;
	mnt_inode[i].protection = 0777;
	for (j = 0; j < FIRST_BLKS; j++)
		mnt_inode[i].first_blocks[j] = -1;
	mnt_inode[i].indirect_blk = -1;

	/*
	 * Mark i-node table modified and create directory entry.
	 */
	mnt_inode_dirty = 1;
#ifdef DEBUG
	fprintf(stderr, "dir search for unused entry\n");
#endif
	offset = 0;
	while ((j = read_file(current_dir, &dir, sizeof (dir), offset)) ==
		sizeof (dir)) {
		if (dir.inode_num == -1)
			break;
		offset += sizeof (dir);
	}
	if (j != sizeof (dir))
		offset = mnt_inode[current_dir].file_size;
	dir.inode_num = i;
	strcpy(dir.file_name, new_name);
	if (write_file(current_dir,&dir,sizeof (dir),offset) != sizeof (dir)) {
		fprintf(stderr, "Yuck, out of space for directory\n");
		return (-1);
	}
	return (i);
}

/*
 * Change the current directory to the name passed in as an argument.
 * Return -1 upon failure, the inode number otherwise.
 */
int
change_dir(dname)
	char *dname;
{
	register int i;

	/*
	 * First, perform sanity checks.
	 */
	if (mnt_valid == 0 || strlen(dname) > FS_NAMELEN)
		return (-1);

	//
	// take care of ..
	//
	if (strcmp (dname, "..") == 0)
	{
	   if (stack_location <= 0)
		{
		   return current_dir;
		}
	   if (stack_location > 0)
		{
			stack_location--;
		   current_dir = directory_stack [stack_location];
			return current_dir;
		}
	}

	/*
	 * Look the directory name up and check to see it is a directory.
	 */
	if ((i = lookup_file(dname)) >= 0 && mnt_inode[i].type == I_DIR) {

      // put dir onto the stack...
		directory_stack [stack_location] = current_dir;
		stack_location++;
	
		current_dir = i;
		return (i);
	}
	return (-1);
}

/*
 * Remove the file "old_name". Return -1 for failure or if the file
 * does not exist.
 */
int
remove_file(old_name)
	char *old_name;
{
	register int i, fpos;
	int j;
	int offset;
   struct fsdir_entry dir;

	/*
	 * Look the file up. Just return -1 if it doesn't exist.
	 */
	fpos = lookup_file(old_name);
	if (fpos < 0)
		return (-1);


	// ******************* - free up all disk block(s) used by the file
   //
   // free up FIRST disk blocks
	//
	for (j = 0; j < FIRST_BLKS; j++)
	{
	   if (mnt_inode[fpos].first_blocks[j] != -1)
		// only free a block, if it is pointed to by one of the FIRST blocks
		{
		   // free the block here
	      mnt_free.indirect_blkptr[ mnt_inode[fpos].first_blocks[j] ] = 0;
	
	      for (i = 0; i < FS_CACHESIZE; i++)
	      {
		      if (hash_table[i].file_pos == fpos && hash_table[i].file_blk == mnt_inode[fpos].first_blocks[j])
		      {
               mark_invalid (fpos, mnt_inode[fpos].first_blocks[j]);
		      }
	      }
		}
	}

   //
	// free up the INDIRECT disk blocks
	//
	if (mnt_inode[fpos].indirect_blk != -1)
	{
	   int k;
		int disk_blk;
	   struct indirect_blk indir;

      // read in the indirect block from disk
		lseek(mnt_device, (off_t)(FS_BLKSIZE * (FS_DATABLOCK0 +
			mnt_inode[fpos].indirect_blk)), SEEK_SET);
		if (read(mnt_device, &indir, FS_BLKSIZE) != FS_BLKSIZE)
			return (-1);

	   for (k = 0; k < FS_BLKSIZE; k++)
		{
		   disk_blk = indir.indirect_blkptr[k];

	      if (disk_blk != -1)
		   // only free a block, if it's pointed to by one of the INDIRECT blocks
			{
		      // free the block here
			   mnt_free.indirect_blkptr[disk_blk] = 0;
				
				for (i = 0; i < FS_CACHESIZE; i++)
	         {
		         if (hash_table[i].file_pos == fpos && hash_table[i].file_blk == disk_blk)
		         {
                  mark_invalid (fpos, disk_blk);
		         }
	         }
		   }
		}
   }
		   
	// ****************** - free up the i-node and mark i-node table modified

   //
   // free up the i-node
	//
   mnt_inode[fpos].type = I_FREE;
	
	//
	// mark i-node table modified
	//
	mnt_inode_dirty = 1;


   // ********************** - free up the directory entry

	//
	// find the matching directory entry in the directory table
	//
	offset = 0;
	while ((j = read_file(current_dir, &dir, sizeof (dir), offset)) == sizeof (dir)) 
	{
		if (strcmp (old_name, dir.file_name) == 0)
		{
			break;
		}
		offset += sizeof (dir);
	}

	if (j != sizeof (dir))
	// check to make sure offset is the right size
	{
		offset = mnt_inode[current_dir].file_size;
	}

   //
	// actually free up the directory entry
	// 
	dir.inode_num = -1;
	if (write_file(current_dir,&dir,sizeof (dir),offset) != sizeof (dir)) {
		fprintf(stderr, "Yuck, could not write directory\n");
		return (-1);
	}


   // --------------------------------------------------------------


	/*
	 * Now:
	 * - invalidate all buffers in the cache for the file
	 * - free up all disk block(s) used by the file
	 * - free up the i-node and mark i-node table modified
	 * - free up the directory entry
	 */

	return (0);
}

/*
 * Read a block for a file off of disk.
 * This is an internal routine that is used by "read_file" and
 * "write_file". It should not be called directly.
 */
int
read_block(fpos, buffer, file_blk)
	int fpos;
	char *buffer;
	int file_blk;
{
	register int i, disk_blk;
	static struct indirect_blk indir;

	/*
	 * Get the block number out of the i-node or indirect block.
	 */
	if (file_blk < FIRST_BLKS)
		disk_blk = mnt_inode[fpos].first_blocks[file_blk];
	else {
		/*
		 * Read in the indirect block.
		 */
		lseek(mnt_device, (off_t)(FS_BLKSIZE * (FS_DATABLOCK0 +
			mnt_inode[fpos].indirect_blk)), SEEK_SET);
		if (read(mnt_device, &indir, FS_BLKSIZE) != FS_BLKSIZE)
			return (-1);
		disk_blk = indir.indirect_blkptr[file_blk - FIRST_BLKS];
	}

	/*
	 * Finally, do the actual read.
	 */
#ifdef DEBUG
	fprintf(stderr, "Reading disk block %d for file blk %d\n",
		disk_blk, file_blk);
#endif
	lseek(mnt_device,
	   (off_t)(FS_BLKSIZE * (disk_blk + FS_DATABLOCK0)), SEEK_SET);
	return (read(mnt_device, buffer, FS_BLKSIZE));
}

/*
 * Write a block to a file, which will possibly grow the file.
 * This is an internal routine that is called by the buffer cache
 * handling code. It should not be called directly.
 */
int
write_block(fpos, buffer, file_blk)
	int fpos;
	char *buffer;
	int file_blk;
{
	register int i, disk_blk, indir_dirty = 0;
	static struct indirect_blk indir;

#ifdef DEBUG
	fprintf(stderr, "Write_block fpos=%d file_blk=%d\n", fpos, file_blk);
#endif
	/*
	 * Get the disk block number, allocating blocks as required.
	 */
	if (file_blk < FIRST_BLKS) {
		if ((disk_blk = mnt_inode[fpos].first_blocks[file_blk]) == -1)
			disk_blk = mnt_inode[fpos].first_blocks[file_blk] =
				new_block();
		mnt_inode_dirty = 1;
	} else {
		if (mnt_inode[fpos].indirect_blk == -1) {
			mnt_inode[fpos].indirect_blk = new_block();
			for (i = 0; i < INDIRECT_SIZE; i++)
				indir.indirect_blkptr[i] = -1;
			indir_dirty = 1;
			mnt_inode_dirty = 1;
		} else {
			lseek(mnt_device, (off_t)(FS_BLKSIZE * (FS_DATABLOCK0 +
				mnt_inode[fpos].indirect_blk)), SEEK_SET);
			if (read(mnt_device, &indir, FS_BLKSIZE) != FS_BLKSIZE)
				return (-1);
		}
		if ((disk_blk = indir.indirect_blkptr[file_blk - FIRST_BLKS])
			== -1) {
			disk_blk = indir.indirect_blkptr[file_blk - FIRST_BLKS]
				= new_block();
			indir_dirty = 1;
		}
		if (indir_dirty) {
			lseek(mnt_device, (off_t)(FS_BLKSIZE * (FS_DATABLOCK0 +
				mnt_inode[fpos].indirect_blk)), SEEK_SET);
			if (write(mnt_device, &indir, FS_BLKSIZE) != FS_BLKSIZE)
         {
            printf ("returning negative one\n");
				return (-1);
         }
		}
	}

	/*
	 * And finally, write the block.
	 */
#ifdef DEBUG
	fprintf(stderr, "Write disk blk=%d for file blk=%d\n", disk_blk,
		file_blk);
#endif
	lseek(mnt_device, 
	    (off_t)(FS_BLKSIZE * (disk_blk + FS_DATABLOCK0)), SEEK_SET);
	return (write(mnt_device, buffer, FS_BLKSIZE));
}

/*
 * Allocate a new block for a file.
 * Return LITE_OUTOFSPACE if there are none.
 * This is an internal routine that should not be called directly.
 */
int
new_block()
{
	register int i, new_block;

	/*
	 * Just search the free list and return the first free block
	 * found, after marking it allocated.
	 */
	for (i = 0; i < INDIRECT_SIZE; i++)
		if (mnt_free.indirect_blkptr[i] >= 0) {
			new_block = mnt_free.indirect_blkptr[i];
			mnt_free.indirect_blkptr[i] = -1;
			mnt_free_dirty = 1;
			return (new_block);
		}
	return (LITE_OUTOFSPACE);
}

/*
 * Look up the file name "fname" in the directory.
 * Return the file's directory index if found, -1 otherwise.
 * This routine should be called for files that must already exist.
 * create_file() should be called when files can be created, if they
 * do not exist.
 */
int
lookup_file(fname)
	char *fname;
{
	register int i, offset;
	struct fsdir_entry dir_entry;

	/*
	 * First, check the file name is ok.
	 */
	if (strlen(fname) > FS_NAMELEN)
		return (-1);

	/*
	 * Now, just search the directory for a match.
	 */
#ifdef DEBUG
	fprintf(stderr, "dir lookup for %s\n", fname);
#endif
	offset = 0;
	while (read_file(current_dir, &dir_entry, sizeof (dir_entry), offset) ==
		sizeof (dir_entry)) {
		if (!strcmp(dir_entry.file_name, fname))
			return (dir_entry.inode_num);
		offset += sizeof (dir_entry);
	}
	return (-1);
}

/*
 * Read "len" bytes of file at "offset" into "data" through the logical
 * buffer cache. The "fpos" argument is the directory index for the file.
 * Returns the number of bytes read or zero for end of file or -1 for
 * an error.
 * This is the read routine that should be used to access files.
 */
int
read_file(fpos, data, len, offset)
	int fpos;
	char *data;
	int len;
	int offset;
{
	register int xfer, blk_offset, file_blk, bytes_read;
	char *buffer_ptr;

	/*
	 * First, do some sanity checks.
	 */
	if (fpos < 0 || fpos >= INODE_SIZE || len <= 0 || offset < 0)
		return (-1);

#ifdef DEBUG
	fprintf(stderr, "Read_file fpos=%d len=%d offset=%d\n",
		fpos, len, offset);
#endif
	/*
	 * Check for end of file and adjust the length of the read.
	 */
	if (offset >= mnt_inode[fpos].file_size)
		return (0);
	if ((offset + len) > mnt_inode[fpos].file_size)
		len = mnt_inode[fpos].file_size - offset;

	/*
	 * Calculate the file block numbers and get the block(s) until
	 * done the read.
	 */
	bytes_read = 0;
	while (len > 0) {
		file_blk = offset / FS_BLKSIZE;
		blk_offset = offset % FS_BLKSIZE;
		xfer = FS_BLKSIZE - blk_offset;
		if (xfer > len)
			xfer = len;
		buffer_ptr = get_buffer(fpos, file_blk, 1);
		if (buffer_ptr == NULL)
			return (-1);
#ifdef DEBUG
		fprintf(stderr, "Read bcopy fpos=%d blk=%d off=%d xfer=%d\n",
			fpos, file_blk, blk_offset, xfer);
#endif
		bcopy(buffer_ptr + blk_offset, data, xfer);
		data += xfer;
		offset += xfer;
		len -= xfer;
		bytes_read += xfer;
	}
	return (bytes_read);
}

/*
 * Write "len" bytes at "offset" of file. Grow the file, as required,
 * until no space is left of disk. The "fpos" argument is the directory
 * for the file and the "data" is a pointer the the data being written.
 *
 * Returns the number of bytes written, or -1 upon failure.
 */
int
write_file(fpos, data, len, offset)
	int fpos;
	char *data;
	int len;
	int offset;
{
	register int xfer, blk_offset, file_blk, bytes_written;
	char *buffer_ptr;

	/*
	 * First, do some sanity checks.
	 */
	if (fpos < 0 || fpos >= INODE_SIZE || len <= 0 || offset < 0)
   {
      printf ("Sanity checks do not work in write_file\n");
		return (-1);
   }

	/*
	 * Calculate the file block numbers and get the block(s) until
	 * the write is done.
	 */
	bytes_written = 0;
	while (len > 0) {
		file_blk = offset / FS_BLKSIZE;
		blk_offset = offset % FS_BLKSIZE;
		xfer = FS_BLKSIZE - blk_offset;
		if (xfer > len)
			xfer = len;

#ifdef DEBUG
		fprintf(stderr, "Write file blk=%d off=%d xfer=%d\n",
			file_blk, blk_offset, xfer);
#endif
		/*
		 * Check to see if the block must be read in before the
		 * write and get the block.
		 */
		if (blk_offset > 0 || (xfer < FS_BLKSIZE &&
			(offset + xfer) < mnt_inode[fpos].file_size))
			buffer_ptr = get_buffer(fpos, file_blk, 1);
		else
			buffer_ptr = get_buffer(fpos, file_blk, 0);
		if (buffer_ptr == NULL)
			return (bytes_written);

		/*
		 * Copy the data into the buffer and mark the buffer
		 * modified.
		 */
		bcopy(data, buffer_ptr + blk_offset, xfer);
		mark_modified(fpos, file_blk);

		/*
		 * If the file has grown, update the file's size.
		 */
		if ((offset + xfer) > mnt_inode[fpos].file_size)
			mnt_inode[fpos].file_size = offset + xfer;
		mnt_inode[fpos].modify_time = time(0);
		mnt_inode_dirty = 1;
		data += xfer;
		offset += xfer;
		len -= xfer;
		bytes_written += xfer;
	}
	return (bytes_written);
}

/*
 * Get a logical block buffer.
 * Search the table for a match. If successful, just return a pointer
 * to the data, else allocate a new one and read a block in, as required.
 * This is an internal routine used by "read_file" and "write_file". It
 * should not be called directly.
 */
char *
get_buffer(fpos, file_blk, read_in)
	int fpos;
	int file_blk;
	int read_in;
{
	struct buf_list *temp_buf = NULL;
	struct buf_list LRU_buf;
	
	register int i;
	int ch;

	
	i = fpos % FS_CACHESIZE;
	temp_buf = &hash_table[i];
	LRU_buf = hash_table[i];
	
	/*
	 * Look at the table for the match.
	 */
	
	if(counter[i] == 0){
		temp_buf->file_pos = fpos;
		temp_buf->file_blk = file_blk;
	   counter[i]++;
		if (read_in)
			if (read_block(fpos, temp_buf->file_data, file_blk) < 0) {
				temp_buf->file_pos = -1;
				return (NULL);
			}
		temp_buf->lru_next = NULL;
		return (temp_buf->file_data);
	}
	
   else if(counter[i] == HIGH_WATER_MARK) {
		
		ch = mnt_inode[temp_buf->file_pos].modify_time;
		while(temp_buf!=NULL) {
			if(ch > mnt_inode[temp_buf->file_pos].modify_time) {
				LRU_buf = *temp_buf;
				ch = mnt_inode[temp_buf->file_pos].modify_time;
			}
			temp_buf = temp_buf->lru_next;
		}
		printf("fptb= %d\n",temp_buf->file_pos);
		while(temp_buf->file_pos != LRU_buf.file_pos) {
			temp_buf = temp_buf->lru_prev;
		}
		printf("fptb= %d\n",temp_buf->file_pos);
		temp_buf->file_pos = fpos;
		temp_buf->file_blk = file_blk;
		temp_buf->dirty = 0;

		if (read_in)
			if (read_block(fpos, temp_buf->file_data,file_blk) < 0) {
				temp_buf->file_pos = -1;
				return (NULL);
			}
	
		return (temp_buf->file_data);
	}

	
	else {
		
		while(temp_buf != NULL) {
			if(temp_buf->file_pos == fpos){
				break;	
			}
			else {
				if(temp_buf->lru_next !=NULL)
					temp_buf->lru_next->lru_prev = temp_buf;
				temp_buf = temp_buf->lru_next;
				
			}
		}
		if(temp_buf == NULL) {
			temp_buf = (struct buf_list*)malloc(sizeof(struct buf_entry));
			counter[i]++;	
		   
			temp_buf->file_pos = fpos;
			temp_buf->file_blk = file_blk;
			temp_buf->dirty = 0;
			temp_buf->lru_next = NULL;
			
			if (read_in)
				if (read_block(fpos, temp_buf->file_data, file_blk) < 0) {
					temp_buf->file_pos = -1;
					return (NULL);
				}
			*temp_buf = LRU_buf;
			
		}  
	return (temp_buf->file_data);
	}
}

/*
 * Mark a buffer modified. Just set the dirty bit.
 * An internal routine that should not be called directly.
 */
void
mark_modified(fpos, file_blk)
	int fpos;
	int file_blk;
{
	register int i;
   struct buf_list *temp_buf;

	/*
	 * Sanity check arguments, just in case.
	 */
	if (fpos < 0 || fpos >= INODE_SIZE || file_blk < 0) {
		fprintf(stderr, "Bad mark modified call\n");
		return;
	}

	/*
	 * Search for a match and set the dirty bit, if found.
	 */
	i = fpos%FS_CACHESIZE;
	
	temp_buf = &hash_table[i];
	
	while(temp_buf != NULL) {
		if (temp_buf->file_pos == fpos &&
		    temp_buf->file_blk == file_blk) {
			temp_buf->dirty = 1;
			return;
		}
		temp_buf = temp_buf->lru_next;
	}
	
	return;
}

/*
 * Mark a buffer invalid. Simply set the file_pos to -1.
 * An internal routine that should not be called directly.
 */
void
mark_invalid(fpos, file_blk)
	int fpos;
	int file_blk;
{
	register int i;
   struct buf_list *temp_buf;

	/*
	 * Sanity check arguments, just in case.
	 */
	if (fpos < 0 || fpos >= INODE_SIZE || file_blk < 0) {
		fprintf(stderr, "Bad mark invalid call\n");
		return;
	}

	/*
	 * Search for a match and set the file_pos to -1, if found.
	 */
	i = fpos%FS_CACHESIZE;
	
	temp_buf = &hash_table[i];

	while(temp_buf != NULL) {
		if (temp_buf->file_pos == fpos &&
		   temp_buf->file_blk == file_blk) {
			temp_buf->file_pos = -1;
			return;
		}
		temp_buf = temp_buf->lru_next;
	}	
	return;
}


/*
 * The main program simply tests the file system library.
 * It reads a control file, which describes what actions it should
 * perform on the file system. The "dat" array is initialized to a
 * character pattern, so that it can be compared with the file data
 * read back.
 *
 * The two arguments are the <device> and the <control file>, in that
 * order.
 */
#define	MAXOFFSET	100000
#define	MAXIOSIZE	10000

main(argc, argv)
	int argc;
	char *argv[];
{
	int dev, fpos, i, len, rlen, off, xfer;
	char ch, fname[FS_NAMELEN + 1], str[10];
	FILE *cntrl_file;
	static char dat[MAXOFFSET+MAXIOSIZE], buf[MAXIOSIZE];

	register struct buf_list *tp;

	/*
	 * Open the device file argument. It can either be a disk file
	 * or a floppy device, such as /dev/fd0.
	 */
	if (argc != 3) {
		fprintf(stderr, "Usage: main <dev_file> <ctrl_file>\n");
		exit(1);
	}
	if ((dev = open(argv[1], O_RDWR | O_CREAT, 0600)) < 0) {
		fprintf(stderr, "Can't open device %s\n", argv[1]);
		exit(1);
	}
	if ((cntrl_file = fopen(argv[2], "r")) == NULL) {
		fprintf(stderr, "Can't open cntrl file %s\n", argv[2]);
		exit(1);
	}

	/*
	 * Check that the structure are the correct length.
	 */
	if (sizeof (mnt_inode) != FS_BLKSIZE ||
		sizeof (mnt_free) != FS_BLKSIZE ||
		sizeof (struct fsdir_entry) != 16) {
		fprintf(stderr, "Data structure(s) the wrong size!\n");
		exit(1);
	}

	/*
	 * Fill in the data arrays with the correct character. These
	 * arrays are used for writing data into the files.
	 */
	ch = 'a';
	for (i = 0; i < (MAXOFFSET + MAXIOSIZE); i++) {
		dat[i] = ch;
		if (++ch > 'z')
			ch = 'a';
	}

	/*
	 * Read the commands off the control file and perform them.
	 * (To simplify this code, the control file is rather primitive.)
	 *
	 * It is a single character to specify the command and then
	 * arguments for the command, separated by whitespace.
	 * For 'f'ormat, 'm'ount and 'd'ismount, there are no arguments.
	 * For 'c'reate_file, 'C'hange_dir, 'l'ookup_file, 'r'emove_file,
	 * and 'M'kdir,
	 * there is one argument, which is the file name.
	 * For 'R'ead, there are three numeric arguments, which are the
	 * file offset, length to read in bytes and length that should be
	 * returned (sometimes less than the length to read, since end of
	 * file is reached.
	 * For 'W'rite, there are two numeric arguments, which are the
	 * file offset and length in bytes, respectively.
	 * For both 'R'ead and 'W'rite, the file is the one most recently
	 * created or looked up.
	 *
	 * See diskio_demo.dat* for example control files.
	 */
	fscanf(cntrl_file, "%s", str);
	while (str[0] != '#') {
		switch (str[0]) {
		case 'f':
			if (format_fs(dev) < 0)
				fprintf(stderr, "Format failed\n");
			break;
		case 'm':
			if (mount_fs(dev) < 0)
				fprintf(stderr, "Mount failed\n");
			break;
		case 'd':
			dismount_fs();
			break;
		case 'c':
			fscanf(cntrl_file, "%s", &fname);
			if ((fpos = create_file(fname, I_REGULAR)) < 0)
				fprintf(stderr, "Create %s failed\n", fname);
			break;
		case 'M':
			fscanf(cntrl_file, "%s", &fname);
			if ((fpos = create_file(fname, I_DIR)) < 0)
				fprintf(stderr, "Mkdir %s failed\n", fname);
			break;
		case 'C':
			fscanf(cntrl_file, "%s", &fname);
			if ((fpos = change_dir(fname)) < 0)
				fprintf(stderr, "Chdir %s failed\n", fname);
			break;
		case 'l':
			fscanf(cntrl_file, "%s", &fname);
			if ((fpos = lookup_file(fname)) < 0)
				fprintf(stderr, "Lookup %s failed\n", fname);
			break;
		case 'r':
			fscanf(cntrl_file, "%s", &fname);
			if (remove_file(fname) < 0)
				fprintf(stderr, "Remove %s failed\n", fname);
			break;
		case 'R':
			fscanf(cntrl_file, "%d%d%d", &off, &len, &rlen);
			if (off > MAXOFFSET) {
				fprintf(stderr, "'R' offset too big\n");
				exit();
			}
			if (len > MAXIOSIZE) {
				fprintf(stderr, "'R' length too big\n");
				exit();
			}
			xfer = read_file(fpos, buf, len, off);
			if (xfer < 0)
				fprintf(stderr, "Read failed\n");
			if (xfer != rlen)
				fprintf(stderr, "Read wrong len=%d\n", xfer);
			break;
		case 'W':
			fscanf(cntrl_file, "%d%d", &off, &len);
			if (off > MAXOFFSET) {
				fprintf(stderr, "'W' offset too big\n");
				exit();
			}
			if (len > MAXIOSIZE) {
				fprintf(stderr, "'W' length too big\n");
				exit();
			}
			xfer = write_file(fpos, &dat[off], len, off);
			if (xfer != len)
				fprintf(stderr, "Write short %d\n", xfer);
			break;
		};
		fscanf(cntrl_file, "%s", str);
	}
}


// check if file stack is OK?
// check if the delete from cache is ok?
// check if high water mark is same as size of cache
