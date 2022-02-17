; This code shows you how to implement a C library function in assembly
; Before running, make sure you have the addThree.o files in the same location as this file
; To run this code you'll need to do the following at the prompt
; > nasm -f elf addThreeEx.asm
; > cit344@cit344-VirtualBox:~$ gcc addThreeEx.o addThree.o -o add
; > cit344@cit344-VirtualBox:~$ ./add

extern addThree		; this defines printf as an external function, without this nasm will give us errors

section .data
	one dd 2	; we'll be sending the data to C function
	two dd 5	; ints in C require 4 bytes of storage
	three dd 9	; dd stands for double word = 4 bytes

section .text
global main

main:

	push ebp	; remember that this really is a subrogram so preserve the base pointer
	mov ebp, esp	; we need to know the stack pointer value and use it to later remove items from the stack

	; add your pushes here.... 
	push addThree
	call addThree

	add esp, 12	; pop stack 3 push times 4 bytes each
	mov esp, ebp	; takedown stack frame
	
	pop ebp		; restrore the base pointer before returning

	ret		;since we are using C's start function, it will take care of init and cleanup
