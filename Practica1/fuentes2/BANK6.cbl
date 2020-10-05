       IDENTIFICATION DIVISION.
       PROGRAM-ID. BANK6.

       ENVIRONMENT DIVISION.
       CONFIGURATION SECTION.
       SPECIAL-NAMES.
           CRT STATUS IS KEYBOARD-STATUS.

       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
           SELECT TARJETAS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS TNUM-E
           FILE STATUS IS FST.

           SELECT F-MOVIMIENTOS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS MOV-NUM
           FILE STATUS IS FSM.

           SELECT F-PROGRAMADAS ASSIGN TO DISK
           ORGANIZATION IS INDEXED
           ACCESS MODE IS DYNAMIC
           RECORD KEY IS PROG-NUM
           FILE STATUS IS FSP.


       DATA DIVISION.
       FILE SECTION.
       FD TARJETAS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "tarjetas.ubd".
       01 TAJETAREG.
           02 TNUM-E      PIC 9(16).
           02 TPIN-E      PIC  9(4).
       FD F-MOVIMIENTOS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "movimientos.ubd".
       01 MOVIMIENTO-REG.
           02 MOV-NUM              PIC  9(35).
           02 MOV-TARJETA          PIC  9(16).
           02 MOV-ANO              PIC   9(4).
           02 MOV-MES              PIC   9(2).
           02 MOV-DIA              PIC   9(2).
           02 MOV-HOR              PIC   9(2).
           02 MOV-MIN              PIC   9(2).
           02 MOV-SEG              PIC   9(2).
           02 MOV-IMPORTE-ENT      PIC  S9(7).
           02 MOV-IMPORTE-DEC      PIC   9(2).
           02 MOV-CONCEPTO         PIC  X(35).
           02 MOV-SALDOPOS-ENT     PIC  S9(9).
           02 MOV-SALDOPOS-DEC     PIC   9(2).

       FD F-PROGRAMADAS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "programadas.ubd".
       01 PROGRAMADA-REG.
           02 PROG-NUM               PIC  9(35).
           02 PROG-ORIGEN            PIC  9(16).
           02 PROG-DESTINO           PIC  9(16).
           02 PROG-ANO               PIC   9(4).
           02 PROG-MES               PIC   9(2).
           02 PROG-DIA               PIC   9(2).
           02 PROG-IMPORTE-ENT       PIC  S9(7).
           02 PROG-IMPORTE-DEC       PIC   9(2).  
           02 MENSUAL                PIC   9(1).  


       WORKING-STORAGE SECTION.
       77 FST                      PIC   X(2).
       77 FSM                      PIC   X(2).
       77 FSP                      PIC   X(2).

       78 BLACK                  VALUE      0.
       78 BLUE                   VALUE      1.
       78 GREEN                  VALUE      2.
       78 CYAN                   VALUE      3.
       78 RED                    VALUE      4.
       78 MAGENTA                VALUE      5.
       78 YELLOW                 VALUE      6.
       78 WHITE                  VALUE      7.

       01 CAMPOS-FECHA.
           05 FECHA.
               10 ANO              PIC   9(4).
               10 MES              PIC   9(2).
               10 DIA              PIC   9(2).
           05 HORA.
               10 HORAS            PIC   9(2).
               10 MINUTOS          PIC   9(2).
               10 SEGUNDOS         PIC   9(2).
               10 MILISEGUNDOS     PIC   9(2).
           05 DIF-GMT              PIC  S9(4).

       01 KEYBOARD-STATUS          PIC  9(4).
           88 ENTER-PRESSED      VALUE     0.
           88 PGUP-PRESSED       VALUE  2001.
           88 PGDN-PRESSED       VALUE  2002.
           88 UP-ARROW-PRESSED   VALUE  2003.
           88 DOWN-ARROW-PRESSED VALUE  2004.
           88 ESC-PRESSED        VALUE  2005.

       77 PRESSED-KEY              PIC   9(4) BLANK ZERO.

       77 LAST-MOV-NUM             PIC  9(35).
       77 LAST-USER-ORD-MOV-NUM    PIC  9(35).
       77 LAST-USER-DST-MOV-NUM    PIC  9(35).

       77 EURENT-USUARIO           PIC  S9(7).
       77 EURDEC-USUARIO           PIC   9(2).
       77 CUENTA-DESTINO           PIC  9(16).
       77 NOMBRE-DESTINO           PIC  X(35).

       77 CENT-SALDO-ORD-USER      PIC  S9(9).
       77 CENT-SALDO-DST-USER      PIC  S9(9).
       77 CENT-IMPOR-USER          PIC  S9(9).

       77 MSJ-ORD                  PIC  X(35) VALUE "Transferimos".
       77 MSJ-DST                  PIC  X(35) VALUE "Nos transfieren".

       77 DIA-USUARIO             PIC   9(2).
       77 MES-USUARIO             PIC   9(2).
       77 ANO-USUARIO             PIC   9(4).
       77 MENSUAL-USUARIO         PIC   A(1) VALUE "n".

       77 LAST-PROG-NUM           PIC  9(35).

       LINKAGE SECTION.
       77 TNUM                     PIC  9(16).

       SCREEN SECTION.
       01 BLANK-SCREEN.
           05 FILLER LINE 1 BLANK SCREEN BACKGROUND-COLOR BLACK.

       01 FILTRO-CUENTA.
           05 FILLER BLANK WHEN ZERO AUTO UNDERLINE
               LINE 12 COL 54 PIC 9(16) USING CUENTA-DESTINO.
           05 FILLER AUTO UNDERLINE
               LINE 14 COL 54 PIC X(15) USING NOMBRE-DESTINO.
           05 FILLER AUTO UNDERLINE
               SIGN IS LEADING SEPARATE
               LINE 16 COL 54 PIC S9(7) USING EURENT-USUARIO.
           05 FILLER UNDERLINE
               LINE 16 COL 63 PIC 9(2) USING EURDEC-USUARIO.
           05 DIA-MIN AUTO UNDERLINE
               LINE 18 COL 54 PIC 9(2) USING DIA-USUARIO.
           05 MES-MIN AUTO UNDERLINE
               LINE 18 COL 57 PIC 9(2) USING MES-USUARIO.
           05 ANO-MIN AUTO UNDERLINE
               LINE 18 COL 60 PIC 9(4) USING ANO-USUARIO.
           05 FILLER UNDERLINE
               LINE 20 COL 54 PIC A(1) USING MENSUAL-USUARIO.

       01 SALDO-DISPLAY.
           05 FILLER SIGN IS LEADING SEPARATE
               LINE 10 COL 33 PIC -9(7) FROM MOV-SALDOPOS-ENT.
           05 FILLER LINE 10 COL 41 VALUE ",".
           05 FILLER LINE 10 COL 42 PIC 99 FROM MOV-SALDOPOS-DEC.
           05 FILLER LINE 10 COL 45 VALUE "EUR".


       PROCEDURE DIVISION USING TNUM.
       INICIO.
           SET ENVIRONMENT 'COB_SCREEN_EXCEPTIONS' TO 'Y'.

           INITIALIZE CUENTA-DESTINO.
           INITIALIZE NOMBRE-DESTINO.
           INITIALIZE EURENT-USUARIO.
           INITIALIZE EURDEC-USUARIO.
           INITIALIZE LAST-MOV-NUM.
           INITIALIZE LAST-USER-ORD-MOV-NUM.
           INITIALIZE LAST-USER-DST-MOV-NUM.

       IMPRIMIR-CABECERA.
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

       MOVIMIENTOS-OPEN.
           OPEN I-O F-MOVIMIENTOS.
           IF FSM <> 00 THEN
               GO TO PSYS-ERR
           END-IF.

       LECTURA-MOVIMIENTOS.
           READ F-MOVIMIENTOS NEXT RECORD AT END GO TO ORDENACION-TRF.
           IF MOV-TARJETA = TNUM THEN
               IF LAST-USER-ORD-MOV-NUM < MOV-NUM THEN
                   MOVE MOV-NUM TO LAST-USER-ORD-MOV-NUM
               END-IF
           END-IF.
           IF LAST-MOV-NUM < MOV-NUM THEN
               MOVE MOV-NUM TO LAST-MOV-NUM
           END-IF.
           GO TO LECTURA-MOVIMIENTOS.

       ORDENACION-TRF.
           CLOSE F-MOVIMIENTOS.

           DISPLAY "Ordenar Transferencia" AT LINE 8 COL 30.
           DISPLAY "Saldo Actual:" AT LINE 10 COL 19.

           DISPLAY "Enter - Confirmar" AT LINE 24 COL 2.
           DISPLAY "ESC - Cancelar" AT LINE 24 COL 61.

           IF LAST-USER-ORD-MOV-NUM = 0 THEN
               GO TO NO-MOVIMIENTOS
           END-IF.

           MOVE LAST-USER-ORD-MOV-NUM TO MOV-NUM.

           PERFORM MOVIMIENTOS-OPEN THRU MOVIMIENTOS-OPEN.
           READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR.
           DISPLAY SALDO-DISPLAY.
           CLOSE F-MOVIMIENTOS.

       INDICAR-CTA-DST.
           DISPLAY "Indica la cuenta destino" AT LINE 12 COL 19.
           DISPLAY "y nombre del titular" AT LINE 14 COL 19.
           DISPLAY "Indique la cantidad a transferir" AT LINE 16 COL 19.
           DISPLAY "." AT LINE 16 COL 62.
           DISPLAY "EUR" AT LINE 16 COL 66.
           DISPLAY "Programar transferencia: Fecha       /  /"
                AT LINE 18 COL 19.
           DISPLAY "Repetir mensualmente? (S/n)" AT LINE 20 COL 19.

           COMPUTE CENT-SALDO-ORD-USER = (MOV-SALDOPOS-ENT * 100)
                                         + MOV-SALDOPOS-DEC.

           ACCEPT FILTRO-CUENTA ON EXCEPTION
           IF ESC-PRESSED THEN
               EXIT PROGRAM
           ELSE
               GO TO INDICAR-CTA-DST
           END-IF.

           *> Verificacion de si es una transferencia programada.
           IF DIA-USUARIO NOT = 0
               IF MES-USUARIO NOT = 0
                   IF ANO-USUARIO NOT = 0
                       *> Es una transferencia programada
                       GO TO PROGRAMAR-TRANSFERENCIA.

           *> Es una transferencia corriente.
           COMPUTE CENT-IMPOR-USER = (EURENT-USUARIO * 100)
                                     + EURDEC-USUARIO.

           IF CENT-IMPOR-USER > CENT-SALDO-ORD-USER THEN
                   DISPLAY "Indique una cantidad menor!!" 
                    AT LINE 20 COL 19
                    WITH BACKGROUND-COLOR RED
                   GO TO INDICAR-CTA-DST
           END-IF.

           GO TO REALIZAR-TRF-VERIFICACION.

       PROGRAMAR-TRANSFERENCIA.

           OPEN I-O F-PROGRAMADAS.
               IF FSP <> 00
                   GO TO PSYS-ERR.
           
           *> Buscamos la ultima transferencia programada.
           MOVE 0 TO LAST-PROG-NUM.
           READ F-PROGRAMADAS NEXT RECORD AT END GO ESCRIBIR-PROG.
               IF LAST-PROG-NUM < PROG-NUM
                   MOVE PROG-NUM TO LAST-PROG-NUM.

       ESCRIBIR-PROG.
           *> Comprobacion de cuenta correcta
           PERFORM VERIFICACION-CTA-CORRECTA 
               THRU VERIFICACION-CTA-CORRECTA.
           *> Escribimos en el fichero de programadas.
           ADD 1 TO LAST-PROG-NUM.

           MOVE LAST-PROG-NUM    TO PROG-NUM.
           MOVE TNUM             TO PROG-ORIGEN.
           MOVE CUENTA-DESTINO   TO PROG-DESTINO.
           MOVE ANO-USUARIO      TO PROG-ANO.
           MOVE MES-USUARIO      TO PROG-MES.
           MOVE DIA-USUARIO      TO PROG-DIA.
           MOVE EURENT-USUARIO   TO PROG-IMPORTE-ENT.
           MOVE EURDEC-USUARIO   TO PROG-IMPORTE-DEC.

           IF MENSUAL-USUARIO IS EQUAL TO "S"
               MOVE 1 TO MENSUAL.

           IF MENSUAL-USUARIO IS EQUAL TO "s"
               MOVE 1 TO MENSUAL
           ELSE
               MOVE 0 TO MENSUAL.

           *> Escritura en fichero.
           WRITE PROGRAMADA-REG.

           *> Darle feedback al usuario.
           GO TO MOSTRAR-RESULTADO-USUARIO.

       NO-MOVIMIENTOS.
           DISPLAY "0" AT LINE 10 COL 51.
           DISPLAY "." AT LINE 10 COL 52.
           DISPLAY "00" AT LINE 10 COL 53.
           DISPLAY "EUR" AT LINE 10 COL 54.

           DISPLAY "Indica la cuenta destino " AT LINE 12 COL 19.
           DISPLAY "y nombre del titular" AT LINE 14 COL 19.
           DISPLAY "Indique la cantidad a transferir" AT LINE 16 COL 19.
           DISPLAY "," AT LINE 16 COL 61.
           DISPLAY "EUR" AT LINE 16 COL 66.
           DISPLAY "Programar transferencia: Fecha       /  /"
                AT LINE 18 COL 19.
           DISPLAY "Repetir mensualmente? (S/n)" AT LINE 20 COL 19.

           ACCEPT FILTRO-CUENTA ON EXCEPTION
           IF ESC-PRESSED THEN
               EXIT PROGRAM
           END-IF.

           DISPLAY "Indique una cantidad menor!!" AT LINE 20 COL 19
            WITH BACKGROUND-COLOR RED.

           GO TO NO-MOVIMIENTOS.

       REALIZAR-TRF-VERIFICACION.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Ordenar Transferencia" AT LINE 08 COL 30.
           DISPLAY "Va a transferir:" AT LINE 11 COL 19.
           DISPLAY EURENT-USUARIO AT LINE 11 COL 38.
           DISPLAY "." AT LINE 11 COL 45.
           DISPLAY EURDEC-USUARIO AT LINE 11 COL 46.
           DISPLAY "EUR de su cuenta" AT LINE 11 COL 49.
           DISPLAY "a la cuenta cuyo titular es" AT LINE 12 COL 19.
           DISPLAY NOMBRE-DESTINO AT LINE 12 COL 48.

           DISPLAY "Enter - Confirmar" AT LINE 24 COL 2.
           DISPLAY "ESC - Cancelar" AT LINE 24 COL 61.

       ENTER-VERIFICACION.
           ACCEPT PRESSED-KEY AT LINE 24 COL 76 ON EXCEPTION 
           IF ESC-PRESSED THEN
               EXIT PROGRAM
           ELSE
               GO TO ENTER-VERIFICACION
           END-IF.

       VERIFICACION-CTA-CORRECTA.
           OPEN I-O TARJETAS.
           IF FST <> 00
              GO TO PSYS-ERR.

           MOVE CUENTA-DESTINO TO TNUM-E.
           READ TARJETAS INVALID KEY GO TO USER-BAD.
           CLOSE TARJETAS.

           PERFORM MOVIMIENTOS-OPEN THRU MOVIMIENTOS-OPEN.
           MOVE 0 TO MOV-NUM.
           MOVE 0 TO LAST-USER-DST-MOV-NUM.

       LECTURA-SALDO-DST.
           READ F-MOVIMIENTOS NEXT RECORD AT END GO TO GUARDAR-TRF.
           IF MOV-TARJETA = CUENTA-DESTINO THEN
               IF LAST-USER-DST-MOV-NUM <= MOV-NUM THEN
                   MOVE MOV-NUM TO LAST-USER-DST-MOV-NUM
               END-IF
           END-IF.

           GO TO LECTURA-SALDO-DST.

       GUARDAR-TRF.
           CLOSE F-MOVIMIENTOS.
           MOVE LAST-USER-DST-MOV-NUM TO MOV-NUM.
           PERFORM MOVIMIENTOS-OPEN THRU MOVIMIENTOS-OPEN.
           IF MOV-NUM = 0 THEN
               MOVE 0 TO MOV-SALDOPOS-ENT
               MOVE 0 TO MOV-SALDOPOS-DEC
           ELSE
               READ F-MOVIMIENTOS INVALID KEY GO PSYS-ERR
           END-IF.

           COMPUTE CENT-SALDO-DST-USER = (MOV-SALDOPOS-ENT * 100)
                                         + MOV-SALDOPOS-DEC.

           MOVE FUNCTION CURRENT-DATE TO CAMPOS-FECHA.

           ADD 1 TO LAST-MOV-NUM.

           MOVE LAST-MOV-NUM   TO MOV-NUM.
           MOVE TNUM           TO MOV-TARJETA.
           MOVE ANO            TO MOV-ANO.
           MOVE MES            TO MOV-MES.
           MOVE DIA            TO MOV-DIA.
           MOVE HORAS          TO MOV-HOR.
           MOVE MINUTOS        TO MOV-MIN.
           MOVE SEGUNDOS       TO MOV-SEG.

           MULTIPLY -1 BY EURENT-USUARIO.
           MOVE EURENT-USUARIO TO MOV-IMPORTE-ENT.
           MULTIPLY -1 BY EURENT-USUARIO.
           MOVE EURDEC-USUARIO TO MOV-IMPORTE-DEC.

           MOVE MSJ-ORD        TO MOV-CONCEPTO.

           SUBTRACT CENT-IMPOR-USER FROM CENT-SALDO-ORD-USER.

           COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-ORD-USER / 100).
           MOVE FUNCTION MOD(CENT-SALDO-ORD-USER, 100)
               TO MOV-SALDOPOS-DEC.

           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.

           ADD 1 TO LAST-MOV-NUM.

           MOVE LAST-MOV-NUM   TO MOV-NUM.
           MOVE CUENTA-DESTINO TO MOV-TARJETA.
           MOVE ANO            TO MOV-ANO.
           MOVE MES            TO MOV-MES.
           MOVE DIA            TO MOV-DIA.
           MOVE HORAS          TO MOV-HOR.
           MOVE MINUTOS        TO MOV-MIN.
           MOVE SEGUNDOS       TO MOV-SEG.

           MOVE EURENT-USUARIO TO MOV-IMPORTE-ENT.
           MOVE EURDEC-USUARIO TO MOV-IMPORTE-DEC.

           MOVE MSJ-DST        TO MOV-CONCEPTO.

           ADD CENT-IMPOR-USER TO CENT-SALDO-DST-USER.
           COMPUTE MOV-SALDOPOS-ENT = (CENT-SALDO-DST-USER / 100).
           MOVE FUNCTION MOD(CENT-SALDO-DST-USER, 100)
               TO MOV-SALDOPOS-DEC.

           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.

           CLOSE F-MOVIMIENTOS.

       P-EXITO.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.

           DISPLAY "Ordenar transferencia" AT LINE 8 COL 30.
           DISPLAY "Transferencia realizada correctamente!" 
               AT LINE 11 COL 19.
           DISPLAY "Enter - Aceptar" AT LINE 24 COL 33.

           GO TO EXIT-ENTER.

       MOSTRAR-RESULTADO-USUARIO.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.

           DISPLAY "Ordenar transferencia" AT LINE 8 COL 30.
           DISPLAY "Transferencia realizada correctamente!" 
               AT LINE 11 COL 19.
           DISPLAY "Con fecha   /  /" AT LINE 13 COL 30.
           DISPLAY DIA-USUARIO AT LINE 13 COL 41.
           DISPLAY MES-USUARIO AT LINE 13 COL 44.
           DISPLAY ANO-USUARIO AT LINE 13 COL 47.
           IF MENSUAL-USUARIO IS EQUAL TO "S" OR
               MENSUAL-USUARIO IS EQUAL TO "s"
                   DISPLAY "Se repetira mensualmente." 
                       AT LINE 15 COL 30.
           DISPLAY "Enter - Aceptar" AT LINE 24 COL 33.

           GO TO EXIT-ENTER.

       PSYS-ERR.
           CLOSE TARJETAS.
           CLOSE F-MOVIMIENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Ha ocurrido un error interno" AT LINE 09 COL 25
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Vuelva mas tarde" AT LINE 11 COL 32
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" AT LINE 24 COL 33.

       EXIT-ENTER.
           ACCEPT PRESSED-KEY AT LINE 24 COL 76
           IF ENTER-PRESSED
               EXIT PROGRAM
           ELSE
               GO TO EXIT-ENTER.

       USER-BAD.
           CLOSE TARJETAS.
           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "La cuenta introducida es incorrecta" 
               AT LINE 9 COL 22
               WITH FOREGROUND-COLOR IS BLACK
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Salir" AT LINE 24 COL 33.
           GO TO EXIT-ENTER.
