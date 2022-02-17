; Carson Seese, CIT344-02, Assignment 4, 12/1/19
; This program takes a string of characters and performs a shift cipher (5-character shift)

; Constants
SYS_EXIT equ 1
SYS_READ equ 3
SYS_WRITE equ 4
STDIN equ 0
STDOUT equ 1

; How many letters to shift
SHIFT equ 5

; What to subtract when greater than 122
RST equ 26

; Length of input text
MAX_LEN equ 5

section .data
	msg1 db "Enter string to encode (5 lower-case characters max): ", 0
	len1 equ $ - msg1

	msg2 db "Encoded string: ", 0
	len2 equ $ - msg2

	newline db 10, 0 ; Newline (dec 10 == '\n')
	len4 equ $ - newline

section .bss
	txt resb MAX_LEN ; The string being processed
	chr resb 1 ; The character being manipulated

section .text
global main

main:
	; Print prompt
	mov eax, SYS_WRITE
	mov ebx, STDOUT
	mov ecx, msg1
	mov edx, len1
	int 0x80

	; Get input
	mov eax, SYS_READ
	mov ebx, STDIN
	mov ecx, txt
	mov edx, MAX_LEN
	int 0x80

	mov edx, txt
	mov ecx, 0 ; Set counter to 0

encode:
	inc ecx ; Increase counter

	mov bl, byte[edx] ; Get first byte of string

	; Skip encoding any characters <97 or >122. If less than 32 (special values) remove.
	cmp bl, 32 ; Check against 32 (dec for 'Space'. Anything lower should be removed)
	jl rm
	cmp bl, 97 ; Check against 97 (dec for 'a')
	jl next
	cmp bl, 122 ; Check against 122 (dec for 'z')
	jg next 

	add bl, SHIFT ; Shift

	cmp bl, 122 ; Check against 122 (dec for 'z')
	jle next ; If less, move on

	sub bl, RST ; If greater, subtract the reset value (26)

next:
	mov byte[edx], bl

	cmp ecx, MAX_LEN ; Check against MAX_LEN
	jge done ; If counter equals MAX_LEN, quit

	inc edx ; Move to next byte
	jmp encode ; Go to start of loop

rm:
	mov bl, 0 ; Set to null
	jmp next

done:	
	; Print final message
	mov eax, SYS_WRITE
	mov ebx, STDOUT
	mov ecx, msg2
	mov edx, len2
	int 0x80

	; Print encoded string
	mov eax, SYS_WRITE
	mov ebx, STDOUT
	mov ecx, txt
	mov edx, MAX_LEN
	int 0x80

	; Print new line
	mov eax, SYS_WRITE
	mov ebx, STDOUT
	mov ecx, newline
	mov edx, len4
	int 0x80