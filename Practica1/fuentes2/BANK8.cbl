       IDENTIFICATION DIVISION.
       PROGRAM-ID. BANK8.

       ENVIRONMENT DIVISION.
       CONFIGURATION SECTION.
       SPECIAL-NAMES.
           CRT STATUS IS KEYBOARD-STATUS.

       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT TARJETAS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS TNUM
           FILE STATUS IS FST.

           SELECT INTENTOS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS INUM
           FILE STATUS IS FSI.


       DATA DIVISION.
       FILE SECTION.
       FD TARJETAS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "tarjetas.ubd".
       01 TAJETAREG.
           02 TNUM      PIC 9(16).
           02 TPIN      PIC  9(4).

       FD INTENTOS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "intentos.ubd".
       01 INTENTOSREG.
           02 INUM      PIC 9(16).
           02 IINTENTOS PIC 9(1).


       WORKING-STORAGE SECTION.
       77 FST                      PIC  X(2).
       77 FSI                      PIC  X(2).

       78 BLACK   VALUE 0.
       78 BLUE    VALUE 1.
       78 GREEN   VALUE 2.
       78 CYAN    VALUE 3.
       78 RED     VALUE 4.
       78 MAGENTA VALUE 5.
       78 YELLOW  VALUE 6.
       78 WHITE   VALUE 7.

       01 CAMPOS-FECHA.
           05 FECHA.
               10 ANO              PIC  9(4).
               10 MES              PIC  9(2).
               10 DIA              PIC  9(2).
           05 HORA.
               10 HORAS            PIC  9(2).
               10 MINUTOS          PIC  9(2).
               10 SEGUNDOS         PIC  9(2).
               10 MILISEGUNDOS     PIC  9(2).
           05 DIF-GMT              PIC S9(4).

       01 KEYBOARD-STATUS           PIC 9(4).
           88 ENTER-PRESSED          VALUE 0.
           88 PGUP-PRESSED        VALUE 2001.
           88 PGDN-PRESSED        VALUE 2002.
           88 UP-ARROW-PRESSED    VALUE 2003.
           88 DOWN-ARROW-PRESSED  VALUE 2004.
           88 ESC-PRESSED         VALUE 2005.

       01 CAMBIO-PIN-REG.
           05 PIN-ORIGINAL         PIC 9(4).
           05 PIN-NUEVO-1          PIC 9(4).
           05 PIN-NUEVO-2          PIC 9(4).

       77 PRESSED-KEY              PIC  9(4).
       77 CHOICE                   PIC  9(1).

       LINKAGE SECTION.
       77 TNUM-PRINCIPAL           PIC  9(16).

       SCREEN SECTION.
       01 BLANK-SCREEN.
           05 FILLER LINE 1 BLANK SCREEN BACKGROUND-COLOR BLACK.

       01 CAMBIO-PIN-ACCEPT.
           05 PIN-OR-ACCEPT BLANK ZERO SECURE LINE 8 COL 50
               PIC 9(4) USING PIN-ORIGINAL.
           05 PIN-1-ACCEPT BLANK ZERO LINE 9 COL 50
               PIC 9(4) USING PIN-NUEVO-1.
           05 PIN-2-ACCEPT BLANK ZERO LINE 10 COL 50
               PIC 9(4) USING PIN-NUEVO-2.

       PROCEDURE DIVISION USING TNUM-PRINCIPAL.

           *> TNUM principal es ambiguo
           MOVE TNUM-PRINCIPAL TO TNUM.
       
       IMPRIMIR-CABECERA.

           SET ENVIRONMENT 'COB_SCREEN_EXCEPTIONS' TO 'Y'
           SET ENVIRONMENT 'COB_SCREEN_ESC'        TO 'Y'

           DISPLAY BLANK-SCREEN.

           DISPLAY "Cajero Automatico UnizarBank" AT LINE 2 COL 26
               WITH FOREGROUND-COLOR IS CYAN.

           MOVE FUNCTION CURRENT-DATE TO CAMPOS-FECHA.

           DISPLAY DIA AT LINE 4 COL 32.
           DISPLAY "-" AT LINE 4 COL 34.
           DISPLAY MES AT LINE 4 COL 35.
           DISPLAY "-" AT LINE 4 COL 37.
           DISPLAY ANO AT LINE 4 COL 38.
           DISPLAY HORAS AT LINE 4 COL 44.
           DISPLAY ":" AT LINE 4 COL 46.
           DISPLAY MINUTOS AT LINE 4 COL 47.

           DISPLAY "Cambio de clave personal" AT LINE 6 COL 20.

       INTRODUCIR-PINES.
           DISPLAY "Introduzca clave actual:" AT LINE 8 COL 20.
           DISPLAY "Introduzca nueva clave:" AT LINE 9 COL 20.
           DISPLAY "Repita la nueva clave:" AT LINE 10 COL 20.
           DISPLAY "Enter - Confirmar" AT LINE 24 COL 0.
           DISPLAY "ESC - Cancelar" AT LINE 24 COL 66.


           ACCEPT CAMBIO-PIN-ACCEPT ON EXCEPTION
               IF ESC-PRESSED
                   GO TO CERRAR-DESCRIPTORES
               ELSE
                   GO TO INTRODUCIR-PINES.

           OPEN I-O TARJETAS.
           IF FST NOT = 00
               GO TO PSYS-ERR.
           READ TARJETAS INVALID KEY GO TO PSYS-ERR.

           OPEN I-O INTENTOS.
           IF FSI NOT = 00
               GO TO PSYS-ERR.
           MOVE TNUM TO INUM.

           READ INTENTOS INVALID KEY GO TO PSYS-ERR.

           IF IINTENTOS = 0
               GO TO PINT-ERR.

           IF PIN-ORIGINAL NOT = TPIN
               GO TO PPIN-ERR.
           
           *> Si has llegado hasta aqui -> EL PIN ORIGINAL COINCIDE
           IF PIN-NUEVO-1 NOT = PIN-NUEVO-2
               GO TO PIN-NUEVO-ERR.

           *> Los codigos PIN nuevos coinciden y el original tambien.
           MOVE PIN-NUEVO-1 TO TPIN.

           REWRITE TAJETAREG INVALID KEY GO TO PSYS-ERR.

           *> La escritura no ha fallado. Se ha guardado el pin.
       
       SUCCESS.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           
           DISPLAY "La clave se ha cambiado correctamente"
               AT LINE 10 COL 20.

           DISPLAY "Enter - Confirmar" AT LINE 24 COL 23.
       
       SUCCESS-ENTER.
           ACCEPT CHOICE AT LINE 24 COL 80 ON EXCEPTION
               IF ENTER-PRESSED
                   GO TO REINICIAR-INTENTOS
               ELSE
                   GO TO SUCCESS-ENTER.

           GO TO REINICIAR-INTENTOS.


       PINT-ERR.

           CLOSE TARJETAS.
           CLOSE INTENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Se ha sobrepasado el numero de intentos"
               AT LINE 9 COL 20
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Por su seguridad se ha bloqueado la tarjeta" 
               AT LINE 11 COL 18
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Acuda a una sucursal" AT LINE 12 COL 30
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" AT LINE 24 COL 33.

           GO TO PINT-ERR-ENTER.       

       PSYS-ERR.

           CLOSE TARJETAS.
           CLOSE INTENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Ha ocurrido un error interno" AT LINE 9 COL 25
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Vuelva mas tarde" AT LINE 11 COL 32
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" AT LINE 24 COL 33.
           GO TO PINT-ERR-ENTER.

       PPIN-ERR.
           SUBTRACT 1 FROM IINTENTOS.
           REWRITE INTENTOSREG INVALID KEY GO TO PSYS-ERR.

           CLOSE TARJETAS.
           CLOSE INTENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "El codigo PIN es incorrecto" AT LINE 9 COL 26
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Le quedan " AT LINE 11 COL 30
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY IINTENTOS AT LINE 11 COL 40
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY " intentos" AT LINE 11 COL 42
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.

           DISPLAY "Enter - Aceptar" AT LINE 24 COL 1.
           DISPLAY "ESC - Cancelar" AT LINE 24 COL 65.

           GO TO PINT-ERR-ENTER.

       PIN-NUEVO-ERR.
           SUBTRACT 1 FROM IINTENTOS.
           REWRITE INTENTOSREG INVALID KEY GO TO PSYS-ERR.

           CLOSE TARJETAS.
           CLOSE INTENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Los codigos PIN nuevos no coinciden." 
               AT LINE 9 COL 26
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.

           DISPLAY "Enter - Aceptar" AT LINE 24 COL 1.
           DISPLAY "ESC - Cancelar" AT LINE 24 COL 65.

       PINT-ERR-ENTER.
           ACCEPT CHOICE AT LINE 24 COL 80 ON EXCEPTION
           IF ENTER-PRESSED
               GO TO IMPRIMIR-CABECERA
           ELSE
               GO TO PINT-ERR-ENTER.

       REINICIAR-INTENTOS.
           MOVE 3 TO IINTENTOS.
           REWRITE INTENTOSREG INVALID KEY GO TO PSYS-ERR.
           
       CERRAR-DESCRIPTORES.
           CLOSE TARJETAS.
           CLOSE INTENTOS.
       