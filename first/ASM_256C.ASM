TLX     DW      0               ;Top_Left_X-int         -> size of box
TLY     DW      0               ;Top_Left_Y-int         -> size of box
BRX     DW      0               ;Bottom_Right_X-int     -> size of box
BRY     DW      0               ;Bottom_Right_Y-int     -> size of box
OKROW   DW      0               ;OK_ROW                 -> rows filled 
OKCOL   DW      0               ;OK_COLumn              -> columns filled
COLOR   DB      0               ;COLOR                  -> color of box
VARA    DW      0               ;VARiable A             -> temporary
VARB    DW      0               ;VARiable B             -> remporary
TWENTY  DW      20              ;TWENTY                 -> constant = 20
TWELVE  DW      12              ;TWELVE                 -> constant = 12
                                                              
        mov     ax,13           ; -> dummy
        mov     ah,00           ;1-> sets mode of display to 320x200 256c
        mov     al,19           ;                                          
        int     10h             ;                                          
        mov     ah,11           ;2-> selects background color to black
        mov     bh,00           ;                                        
        mov     bl,00           ;                                        
        int     10h             ;                                          
        mov     ah,11           ;3-> selects PALLETTE of color of dots
        mov     bh,01           ;                                          
        mov     bl,00           ;                                          
        int     10h             ;                                          
                                                                  
START:  mov     ax,okrow        ;1-> get Top_Left_Y-int (TLY)   
        mul     twelve          ;     (tly = okrow x 12)                      
        mov     tly,ax          ;                                 
        mov     ax,okrow        ;2-> get Bottom_Right_Y-int (BRY)
        inc     ax              ;     (bry = (okrow +1) x 12)                            
        mul     twelve          ;                                 
        mov     bry,ax          ;                                 
        mov     ax,okcol        ;3-> get Top_Left_X-int (TLX)                
        mul     twenty          ;     (tlx = okcol x 20)        
        mov     tlx,ax          ;                                 
        mov     ax,okcol        ;4-> get Bottom_Right_X-int (BRX)
        inc     ax              ;     (brx = (okcol + 1) x 20)  
        mul     twenty          ;                                 
        mov     brx,ax          ;                                 
                                                                  
        mov     vara,tlx        ; -> assign Top_Left_X-int to 'vara' (column)   
        mov     varb,tly        ; -> assign Top_Left_Y-int to 'varb' (row)
LPCOL:  mov     al,color        ; -> set the color
        mov     ah,12           ; -> set mode to dot-draw                 
        mov     dx,varb         ; -> set dx (row) to 'varb'               
        mov     cx,vara         ; -> set cx (column) to 'vara'            
        int     10h             ; -> dos interrupt - draw                 
        inc     vara            ; -> increase 'vara' (column)             
        mov     ax,vara         ; -> prepare for comparison               
        cmp     ax,brx          ; -> compare 'vara' to Bottom_Right_X-int 
        je      LPROW           ; -> if equal jump to LPROW (loop row)
        jmp     LPCOL           ; -> unconditional jump to LPCOL (loop column)             
LPROW:  mov     vara,tlx        ; -> assign Top_Left_X-int to 'vara' (column)
        inc     varb            ; -> increase varb (row)                      
        mov     ax,varb         ; -> prepare for comparison                   
        cmp     ax,bry          ; -> compare 'varb' to Bottom_Right_Y-int     
        je      INCCOL          ; -> if equal jump to INCCOL (increase column)           
        jmp     LPCOL           ; -> unconditional jump to LPCOL (loop column)
                                                                          
INCCOL: inc     color           ; -> increase color variable             
        inc     okcol           ; -> increase 'okcol' (rows done)        
        cmp     okcol,16        ; -> compare 'okcol' to 16                 
        je      INCROW          ; -> if yes jump to INCROW (increase row)
        jmp     START           ; -> unconditional jump to START         
INCROW: mov     okcol,0         ; -> if 16 columns done, reset 'okcol' to 0
        inc     okrow           ; -> increase 'okrow' (rows done)     
        cmp     okrow,16        ; -> compare 'okrow' to 16            
        je      EXIT            ; -> if equal jump to EXIT, program ends
        jmp     START           ; -> unconditional jump to START      
                                                                      
EXIT:   mov     ah,4Ch          ; -> exit to DOS
        int     21h             ; -> END OF PROGRAM                       
