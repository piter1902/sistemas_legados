       IDENTIFICATION DIVISION.
       PROGRAM-ID. BANK9.

       ENVIRONMENT DIVISION.
       CONFIGURATION SECTION.
       SPECIAL-NAMES.
           CRT STATUS IS KEYBOARD-STATUS.

       INPUT-OUTPUT SECTION.
       FILE-CONTROL.
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
       FD F-MOVIMIENTOS
           LABEL RECORD STANDARD
           VALUE OF FILE-ID IS "movimientos.ubd".
       01 MOVIMIENTO-REG.
           02 MOV-NUM               PIC  9(35).
           02 MOV-TARJETA           PIC  9(16).
           02 MOV-ANO               PIC   9(4).
           02 MOV-MES               PIC   9(2).
           02 MOV-DIA               PIC   9(2).
           02 MOV-HOR               PIC   9(2).
           02 MOV-MIN               PIC   9(2).
           02 MOV-SEG               PIC   9(2).
           02 MOV-IMPORTE-ENT       PIC  S9(7).
           02 MOV-IMPORTE-DEC       PIC   9(2).
           02 MOV-CONCEPTO          PIC  X(35).
           02 MOV-SALDOPOS-ENT      PIC  S9(9).
           02 MOV-SALDOPOS-DEC      PIC   9(2).
       
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
       77 FSM                       PIC   X(2).
       77 FSP                       PIC   X(2).

       78 BLACK                     VALUE    0.
       78 BLUE                      VALUE    1.
       78 GREEN                     VALUE    2.
       78 CYAN                      VALUE    3.
       78 RED                       VALUE    4.
       78 MAGENTA                   VALUE    5.
       78 YELLOW                    VALUE    6.
       78 WHITE                     VALUE    7.

       01 CAMPOS-FECHA.
           05 FECHA.
               10 ANO               PIC   9(4).
               10 MES               PIC   9(2).
               10 DIA               PIC   9(2).
           05 HORA.
               10 HORAS             PIC   9(2).
               10 MINUTOS           PIC   9(2).
               10 SEGUNDOS          PIC   9(2).
               10 MILISEGUNDOS      PIC   9(2).
           05 DIF-GMT               PIC  S9(4).

       01 KEYBOARD-STATUS           PIC   9(4).
           88 ENTER-PRESSED         VALUE    0.
           88 PGUP-PRESSED          VALUE 2001.
           88 PGDN-PRESSED          VALUE 2002.
           88 UP-ARROW-PRESSED      VALUE 2003.
           88 DOWN-ARROW-PRESSED    VALUE 2004.
           88 ESC-PRESSED           VALUE 2005.
       77 PRESSED-KEY               PIC   9(4) BLANK ZERO.
       77 PROG-VALIDA               PIC   9(1).
       77 FECHA-HOY                 PIC   9(8). 
       77 FECHA-PROG                PIC   9(8).
       77 LAST-MOV-NUM              PIC   9(35). 

       PROCEDURE DIVISION.
       IMPRIMIR-CABECERA.

           MOVE FUNCTION CURRENT-DATE TO CAMPOS-FECHA.


       PCONSULTA-MOV.

           OPEN INPUT F-MOVIMIENTOS.
               IF FSM <> 00
                   GO TO PSYS-ERR.
             
           OPEN INPUT F-PROGRAMADAS.
               IF FSM <> 00
                   GO TO PSYS-ERR.


       LEER-PRIMEROS.
           READ F-PROGRAMADAS NEXT RECORD AT END GO WAIT-ORDER.
               *> Registro cargado
               MOVE 1 TO PROG-VALIDA.

               PERFORM FILTRADO THRU FILTRADO.
       LECTURA-SALDO.
               IF PROG-VALIDA = 1
                   MOVE 0 TO LAST-MOV-NUM.
                   READ F-MOVIMIENTOS NEXT RECORD AT END GO ESCRIBIR-TRANSFERENCIA.
                   IF MOV-TARJETA = PROG-ORIGEN
                       IF LAST-MOV-NUM < MOV-NUM
                           MOVE MOV-NUM TO LAST-MOV-NUM.
                       GO LECTURA-SALDO.

       
       ESCRIBIR-TRANSFERENCIA
           

       WAIT-ORDER.

           ACCEPT PRESSED-KEY AT LINE 24 COL 80 ON EXCEPTION 

              IF ESC-PRESSED THEN
                  CLOSE F-MOVIMIENTOS
                  EXIT PROGRAM
              END-IF

              IF PGDN-PRESSED THEN
                  GO TO FLECHA-ABAJO
              END-IF

              IF PGUP-PRESSED THEN
                  GO TO FLECHA-ARRIBA
              END-IF

           END-ACCEPT.

           GO TO WAIT-ORDER.

       FLECHA-ABAJO.
           MOVE REGISTROS-EN-PANTALLA(MOV-EN-PANTALLA) TO MOV-NUM.
           READ F-MOVIMIENTOS INVALID KEY GO WAIT-ORDER.
           GO TO LEER-VIEJO.

       FLECHA-ARRIBA.
           MOVE REGISTROS-EN-PANTALLA(1) TO MOV-NUM.
           READ F-MOVIMIENTOS INVALID KEY GO WAIT-ORDER.
           GO TO LEER-NUEVO.

       LEER-VIEJO.
           READ F-MOVIMIENTOS PREVIOUS RECORD
               AT END GO WAIT-ORDER.

               MOVE 1 TO MOV-VALIDO.
               PERFORM FILTRADO THRU FILTRADO.

               IF MOV-VALIDO = 1
                   MOVE 2 TO MOV-VALIDO
                   GO TO CONTROL-PANTALLA
               ELSE
                   GO TO LEER-VIEJO.

       LEER-NUEVO.
           READ F-MOVIMIENTOS NEXT RECORD
               AT END GO WAIT-ORDER.

               MOVE 1 TO MOV-VALIDO.
               PERFORM FILTRADO THRU FILTRADO.

               IF MOV-VALIDO = 1
                   MOVE 3 TO MOV-VALIDO
                   GO TO CONTROL-PANTALLA
               ELSE
                   GO TO LEER-NUEVO.

       CONTROL-PANTALLA.
           IF MOV-VALIDO = 2 THEN
               MOVE 0 TO MOV-VALIDO
               PERFORM REORDENAR-1 THRU REORDENAR-1
               GO TO WAIT-ORDER
           ELSE
               IF MOV-VALIDO = 3 THEN
                   MOVE 0 TO MOV-VALIDO
                   PERFORM REORDENAR-2 THRU REORDENAR-2
                   GO TO WAIT-ORDER
               ELSE
                   GO TO WAIT-ORDER
               END-IF
           END-IF.

       REORDENAR-1.
           MOVE 2 TO CONTADOR.
           MOVE MOV-EN-PANTALLA TO ITERACIONES.
           SUBTRACT 1 FROM ITERACIONES.

           PERFORM ITERACIONES TIMES
               MOVE REGISTROS-EN-PANTALLA(CONTADOR) TO COPIA-MOV
               SUBTRACT 1 FROM CONTADOR
               MOVE COPIA-MOV TO REGISTROS-EN-PANTALLA(CONTADOR)
               ADD 2 TO CONTADOR
           END-PERFORM.

           MOVE MOV-NUM TO REGISTROS-EN-PANTALLA(MOV-EN-PANTALLA).
           PERFORM MOSTRAR-TABLA THRU MOSTRAR-TABLA.

           GO TO WAIT-ORDER.

       REORDENAR-2.
           MOVE MOV-EN-PANTALLA TO CONTADOR.
           SUBTRACT 1 FROM CONTADOR.
           MOVE MOV-EN-PANTALLA TO ITERACIONES.
           SUBTRACT 1 FROM ITERACIONES.


           PERFORM ITERACIONES TIMES
               MOVE REGISTROS-EN-PANTALLA(CONTADOR) TO COPIA-MOV
               ADD 1 TO CONTADOR
               MOVE COPIA-MOV TO REGISTROS-EN-PANTALLA(CONTADOR)
               SUBTRACT 2 FROM CONTADOR
           END-PERFORM.

           MOVE MOV-NUM TO REGISTROS-EN-PANTALLA(1).

           PERFORM MOSTRAR-TABLA THRU MOSTRAR-TABLA.

           GO TO WAIT-ORDER.

       MOSTRAR-TABLA.
           MOVE 8 TO LINEA-MOV-ACTUAL.
           MOVE 1 TO CONTADOR.

           PERFORM MOV-EN-PANTALLA TIMES
               MOVE REGISTROS-EN-PANTALLA(CONTADOR) TO MOV-NUM
               PERFORM READ-MOVIMIENTO THRU READ-MOVIMIENTO
               PERFORM MOSTRAR-MOVIMIENTO THRU MOSTRAR-MOVIMIENTO
               ADD 1 TO LINEA-MOV-ACTUAL
               ADD 1 TO CONTADOR
           END-PERFORM.

       READ-MOVIMIENTO.
           READ F-MOVIMIENTOS INVALID KEY GO TO PSYS-ERR.

       PSYS-ERR.
           CLOSE F-MOVIMIENTOS.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           DISPLAY "Ha ocurrido un error interno" AT LINE 9 COL 25
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Vuelva mas tarde" AT LINE 11 COL 32
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" AT LINE 24 COL 33.

       EXIT-ENTER.
           ACCEPT PRESSED-KEY AT LINE 24 COL 80
           IF ENTER-PRESSED
               EXIT PROGRAM
           ELSE
               GO TO EXIT-ENTER.


       FILTRADO.

           COMPUTE FECHA-HOY = (ANO * 10000)
                               + (MES * 100)
                               + DIA.
                              
           COMPUTE FECHA-PROG = (PROG-ANO * 10000)
                               + (PROG-MES * 100)
                               + PROG-DIA.

           IF FECHA-HOY >= FECHA-PROG
               MOVE 1 TO PROG-VALIDA
           ELSE
               MOVE 0 TO PROG-VALIDA.


       MOSTRAR-MOVIMIENTO.

           MOVE FUNCTION MOD(LINEA-MOV-ACTUAL, 2)
               TO MODULO-LIN-ACTUAL.

           IF MODULO-LIN-ACTUAL = 0
               DISPLAY FILA-MOVIMIENTO-PAR
           ELSE
               DISPLAY FILA-MOVIMIENTO-IMPAR.
