#!/usr/bin/perl
# --- Michael Bochenek [ID: 980492820 (0041056)] ---

use File::Find;
@tree=();
@oldnews=();
@oldnews_numbers=();
@new_dir=();

# --- check if a valid argument is given, if not, exit with error ---
opendir (Ptr, $ARGV[0]) or die print ("Invalid Directory.\n"), exit(0); 

# --- get all subdirectories of ARGV and store them in array _tree_ ---
find (\&find_dir,$ARGV[0]);


# --- open _oldnews_ and read it all into an array (excluding "\n") ---
open (file_pointer, "oldnews") or die print ("no oldnews file"), exit(0); 
$lines = 0;                              # number of lines counter
while ( $_ = <file_pointer> ) {          # loop while not EOF
   chop ($_);                            # get rid of the '\n'
   $loc_of_colon = index ($_, ":");      # get integer position of ':'
   $string_length = length $_;           # get length of string
   if ($loc_of_colon eq string_length) { # if there's nothing after the ':'
      $oldnews_numbers [$lines] = 0;     # integer after ':' is zero
      $oldnews [$lines] = $_;            # put newsgroup + ':' into array
   } else {
      $oldnews_numbers [$lines] = substr ($_, $loc_of_colon+1, $string_length);
      $oldnews [$lines] = substr ($_, 0, $loc_of_colon+1);
      # if there is something after the ':', then cut the line into 2 parts
      # 1) with the newsgroup name + ':', 2) the number after the ':'
   }
   $lines++;                             # increment lines counter
}

# --- loop through all subdirectories in ARGV and make directories ---
# --- look like newsgroups and add a ";" at the end ---
$num_of_dir = 0;
foreach $temp ( @tree ) {      # loop through all elements in array
   $temp =~ s/\//\./g;         # change '/' to '.' globally
   $temp =~ s/^\.//;           # get rid of the first '.' if it's there
   $temp =~ s/$/\:/;           # add a ':' at the end
   $num_of_dir++;              # count number of directories
}

# --- loop through all _oldnews_ and all _tree_ and find items in _tree_ ---
# --- that are NOT found in _oldnews_ and and ask the user if they would ---
# --- like to add this item to to newsgroup list.  If user answer 'yes' ---
# --- then add it to array _new_dir_. ---
# --- yep - 980492820 ---
foreach $new ( @tree ) {             # loop through all directories
   $i = 0;                           # counter of matches found

   foreach $existing ( @oldnews) {   # loop through all in _oldnews_
      if ( $new eq $existing ) {     # if there is a match
         $i++;                       # increase counter for matches
      }
   }

   if ( $i eq "0" ) {                # if NO matches were made

      $new =~ s/$\;//;               # get rid of ';' <-- doesn't do dick
      $tempx = $new;                 # temp for printint only
      chop $tempx;                   # get rid of ':' for printing only 
      print "\n", $tempx, "\n";      # print the "NEW" newsgroup

      while ( $i eq "0" ) {          # start looping
         print "Do you want to add this newsgroup to your news list?\n";
         print "(Please enter \"Yes\" or \"No\" as your response) ??? ";
         $answer = <STDIN>;          # get user answer
         chop ( $answer );           # get rid of the '\n'
         if ($answer eq "Yes") {     # if user answers exactly "Yes"
            push (@new_dir, $new);   # add "NEW" newsgroup to list of new
            $i++;                    # set flag to stop looping
         }
         if ($answer eq "No") {      # if user answer exactly "No"
            $i++;                    # set flag to stop looping 
         }
      }
   }
}

# --- write out the items that the user wishes to add to _oldnews_ ---
open (file_pointer, ">> oldnews");     # open file for appending
foreach $temp ( @new_dir ) {           # loop through all "NEW" newsgroups
   print file_pointer $temp, ":\n";    # write out newsgroup
   $oldnews [$lines] = $temp;          # add newsgroup to array
   $oldnews_numbers [$lines] = 0;      # add zero to the other array
   $lines++;                           # increase array index
}

# --- process the newsgroups in the the oldnews file ---
$i = 0;                            # index to traverse all in _oldnews_
foreach $path_fake ( @oldnews ) {  # traverse paths in _oldnews_
   $new_counter = 0;               # counts number of "NEWER" articles
   @newsgroup=();                  # clear list of non-dir-files array
   $path = $path_fake;             # copy directory name
   chop $path;                     # get rid of the ':' at end
   print $path, " ";               # print out the newsgroup to screen
   $path =~ s/\./\//g;             # replace all dots with '/'

   $largest = 0;
   find (\&find_non_dir, $path);      # get all non-dir-files in directory
   foreach $filename ( @newsgroup ) { # loop through all non-dir-files
      if ( $oldnews_numbers [$i] < $filename ) {
         # KEY!!! : if filename is greater than ....
         $new_counter++;                     # increase NEW counter
         $largest = $filename;               # set filename as the largest 
      } 
   }

   if ($largest != 0) {
      $oldnews_numbers [$i] = $largest;      # prepare array for file-output 
   }
 
   if ($oldnews_numbers [$i] == 0) {         # if largest was zero
      if ($new_counter == 0) {               # if no NEW articles
         $oldnews_numbers [$i] = "";         # change in array "0" --> ""
      }
   }

   print $new_counter, "\n";                 # print NEW articles counter
   $i++;
}

# --- output stuff to file, newsgroup name followed by latest article ---
$lines = 0;                        # lines counter
close (file_pointer);              # close previously used pointer
open (file_pointer, "> oldnews");  # open the file for overwrite output
foreach $temp ( @oldnews ) {       # loop through all in _oldnews_
   print file_pointer $temp, $oldnews_numbers [$lines], "\n";
   # print the stuff to the file, and increment index counter (lines)
   $lines++;
}

# - will not work properly if a newsgroup is not within the root 
#   specified by the argument
# - will not work properly if that thing that removes '.' at the start
#   is used, since there is not way of putting the '/' back.

# --------------------- PROCEDURES ---------------------
sub find_dir { # <-- gets all directory names
   if ( -d ) {
      @next = $File::Find::name;
      push(@tree, @next);
   }
}

sub find_non_dir { # <-- gets all non-directory names
   if ( !(-d) && ( $File::Find::dir eq $path) ) {
      @next = substr ($File::Find::name, length ($path) + 1);
      push(@newsgroup, @next);
   }
}
