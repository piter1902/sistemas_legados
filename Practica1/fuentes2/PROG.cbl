       IDENTIFICATION DIVISION.
       PROGRAM-ID. PROG.

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
       77 LAST-MOV-NUM-DEST         PIC   9(35).
       77 PUNTERO                   PIC   9(35).
       77 LAST-MOV-NUM-GLOBAL       PIC   9(35).
       77 SALDO-USUARIO-TOT         PIC   S9(9).
       77 SALDO-USUARIO-ENT         PIC   S9(9).
       77 SALDO-USUARIO-DEC         PIC   9(9).

       77 DEST-SALDOPOS-ENT         PIC  S9(9).
       77 DEST-SALDOPOS-DEC         PIC   9(2).
       77 INCREMENTO                PIC   9(35).
       
       SCREEN SECTION.
       01 BLANK-SCREEN.
           05 FILLER LINE 1 BLANK SCREEN BACKGROUND-COLOR BLACK.

       PROCEDURE DIVISION.
       IMPRIMIR-CABECERA.
           INITIALIZE LAST-MOV-NUM.
           INITIALIZE LAST-MOV-NUM-DEST.
           INITIALIZE LAST-MOV-NUM-GLOBAL.
           MOVE FUNCTION CURRENT-DATE TO CAMPOS-FECHA.


       PCONSULTA-MOV.

           OPEN I-O F-MOVIMIENTOS.
               IF FSM <> 00
                   GO TO PSYS-ERR.
             
           OPEN I-O F-PROGRAMADAS.
               IF FSP <> 00
                   GO TO PSYS-ERR.


       LEER-PRIMEROS.
           READ F-PROGRAMADAS NEXT RECORD AT END GO FIN-PROGRAMADAS.
               *> Registro cargado
               MOVE 1 TO PROG-VALIDA.
               *> Se reinicia valor para evitar problemas

               PERFORM FILTRADO THRU FILTRADO.
       LECTURA-SALDO.
               *> Si no es válida, seguimos con siguiente programada
               IF PROG-VALIDA = 0
                   GO TO LEER-PRIMEROS.

                MOVE 0 TO LAST-MOV-NUM.
                MOVE 0 TO LAST-MOV-NUM-DEST.
                MOVE 0 TO LAST-MOV-NUM-GLOBAL.
                MOVE 0 TO MOV-NUM.
                MOVE 0 TO INCREMENTO.
                CLOSE F-MOVIMIENTOS.
                OPEN I-O F-MOVIMIENTOS.
                IF FSM <> 00
                   GO TO PSYS-ERR.
       BUSQUEDA-MAYOR.
                   READ F-MOVIMIENTOS NEXT RECORD AT END 
                   GO ESCRIBIR-TRANSFERENCIA.
                   *> Buscamos el número máximo de movimiento del orig.
                   IF MOV-TARJETA = PROG-ORIGEN
                       IF LAST-MOV-NUM < MOV-NUM
                           ADD 1 TO INCREMENTO
                           MOVE MOV-NUM TO LAST-MOV-NUM    
                   END-IF.
                   *> Buscamos el número máximo de movimiento del dest.
                   IF MOV-TARJETA = PROG-DESTINO
                       IF LAST-MOV-NUM-DEST < MOV-NUM
                           MOVE MOV-NUM TO LAST-MOV-NUM-DEST
                   END-IF.
                   *> Busqueda del ultimo mov-num.
                   IF MOV-NUM > LAST-MOV-NUM-GLOBAL
                       MOVE MOV-NUM TO LAST-MOV-NUM-GLOBAL
                   GO BUSQUEDA-MAYOR.
       
       ESCRIBIR-TRANSFERENCIA.

           *> Es redundante?
        *>    IF FSP <> 00
        *>       GO TO PSYS-ERR.

           *> Señala al ult. movimiento de la cuenta origen.
           MOVE LAST-MOV-NUM TO PUNTERO.
           *> Apuntamos al sitio de movimientos que se desea
           CLOSE F-MOVIMIENTOS.
           OPEN I-O F-MOVIMIENTOS.
               IF FSM <> 00
                   GO TO PSYS-ERR.
       APUNTAR-BUCLE.
           READ F-MOVIMIENTOS NEXT RECORD AT END GO TO APUNTAR-FIN.
           IF MOV-NUM NOT = PUNTERO
               GO TO APUNTAR-BUCLE.
       
       APUNTAR-FIN.
           *> Evitamos problemas de no existencia.
           IF MOV-NUM = 0
               MOVE 0 TO MOV-SALDOPOS-ENT
               MOVE 0 TO MOV-SALDOPOS-DEC
           END-IF.

           *> Saldo-usuario-xxx guarda el saldo de la cuenta origen.
           MOVE MOV-SALDOPOS-ENT TO SALDO-USUARIO-ENT.
           MOVE MOV-SALDOPOS-DEC TO SALDO-USUARIO-DEC.   
           SUBTRACT PROG-IMPORTE-ENT FROM SALDO-USUARIO-ENT.
           SUBTRACT PROG-IMPORTE-DEC FROM SALDO-USUARIO-DEC.
           *> Saldo(Cuenta_Origen) - cantidad(programada)
           COMPUTE SALDO-USUARIO-TOT = 
                   (SALDO-USUARIO-ENT) * 100 + SALDO-USUARIO-DEC.

           *> Si no hay saldo suficiente -> Dejar la programada ahi.
           IF SALDO-USUARIO-TOT < 0
               GO TO LEER-PRIMEROS.
               
           *> Comprobacion de transferencia mensual.
           IF MENSUAL = 0
               *> Eliminar programada del fichero
               DELETE F-PROGRAMADAS RECORD INVALID KEY GO TO PSYS-ERR
           ELSE
               *> Modificar el mes (+1)
               IF PROG-MES = 12
                   MOVE 1 TO PROG-MES
                   ADD  1 TO PROG-ANO
               ELSE
                   ADD  1 TO PROG-MES
               *> Reescribimos la programada con el nuevo mes.
               REWRITE PROGRAMADA-REG INVALID KEY GO TO PSYS-ERR
           END-IF.

           *> Escribimos transferencia de la cuenta origen.
       ESCRITURA-ORIGEN.
           ADD 1 TO LAST-MOV-NUM-GLOBAL.

           MOVE LAST-MOV-NUM-GLOBAL     TO MOV-NUM.
           MOVE PROG-ORIGEN             TO MOV-TARJETA.
           MOVE ANO                     TO MOV-ANO.
           MOVE MES                     TO MOV-MES.
           MOVE DIA                     TO MOV-DIA.
           MOVE HORAS                   TO MOV-HOR.
           MOVE MINUTOS                 TO MOV-MIN.
           MOVE SEGUNDOS                TO MOV-SEG.

           *> El origen tiene que ser negativo (transfiere)
           MULTIPLY -1 BY PROG-IMPORTE-ENT.
           MOVE PROG-IMPORTE-ENT           TO MOV-IMPORTE-ENT.
           MULTIPLY -1 BY PROG-IMPORTE-ENT.
           MOVE PROG-IMPORTE-DEC           TO MOV-IMPORTE-DEC.
        *>    MOVE "Transferencia programada" TO MOV-CONCEPTO.
           MOVE LAST-MOV-NUM-GLOBAL               TO MOV-CONCEPTO.
           
           MOVE SALDO-USUARIO-ENT       TO MOV-SALDOPOS-ENT.
           MOVE SALDO-USUARIO-DEC       TO MOV-SALDOPOS-DEC.
           *> Escritura
           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.

           *> Transferencia lista para buscar cuenta destino.
           *> El máximo movimiento de prog-destino es LAST-MOV-NUM-DEST
       ESCRITURA-DESTINO.

           *> Señala al ult. movimiento de la cuenta origen.
           MOVE LAST-MOV-NUM TO PUNTERO.
           *> Apuntamos al sitio de movimientos que se desea
           CLOSE F-MOVIMIENTOS.
           OPEN I-O F-MOVIMIENTOS.
               IF FSM <> 00
                   GO TO PSYS-ERR.
       APUNTAR-BUCLE2.
           READ F-MOVIMIENTOS NEXT RECORD AT END GO TO APUNTAR-FIN2.
           IF MOV-NUM NOT = PUNTERO
               GO TO APUNTAR-BUCLE2.
       
       APUNTAR-FIN2.
           *> Evitamos problemas de no existencia.
           IF MOV-NUM = 0
               MOVE 0 TO MOV-SALDOPOS-ENT
               MOVE 0 TO MOV-SALDOPOS-DEC
           END-IF.
           *> Calculos de saldo restante.
           MOVE 0 TO DEST-SALDOPOS-ENT
           MOVE 0 TO DEST-SALDOPOS-DEC
           COMPUTE DEST-SALDOPOS-ENT = 
               PROG-IMPORTE-ENT + MOV-SALDOPOS-ENT.
           COMPUTE DEST-SALDOPOS-DEC = 
               PROG-IMPORTE-DEC + MOV-SALDOPOS-DEC.

           *> Escritura.
           ADD 1 TO LAST-MOV-NUM-GLOBAL.

           MOVE LAST-MOV-NUM-GLOBAL          TO MOV-NUM.
           MOVE PROG-DESTINO                 TO MOV-TARJETA.
           MOVE ANO                          TO MOV-ANO.
           MOVE MES                          TO MOV-MES.
           MOVE DIA                          TO MOV-DIA.
           MOVE HORAS                        TO MOV-HOR.
           MOVE MINUTOS                      TO MOV-MIN.
           MOVE SEGUNDOS                     TO MOV-SEG.

           MOVE PROG-IMPORTE-ENT           TO MOV-IMPORTE-ENT.
           MOVE PROG-IMPORTE-DEC           TO MOV-IMPORTE-DEC.
           *>    MOVE "Transferencia programada" TO MOV-CONCEPTO.
           MOVE  LAST-MOV-NUM-GLOBAL       TO MOV-CONCEPTO.

           MOVE DEST-SALDOPOS-ENT       TO MOV-SALDOPOS-ENT.
           MOVE DEST-SALDOPOS-DEC       TO MOV-SALDOPOS-DEC.
           *> Escritura
           WRITE MOVIMIENTO-REG INVALID KEY GO TO PSYS-ERR.

           *> Cerramos y volvemos a abrir.
        *>    CLOSE F-MOVIMIENTOS.

        *>    OPEN I-O F-MOVIMIENTOS.
        *>        IF FSM <> 00
        *>            GO TO PSYS-ERR.

           *> Volver a leer programadas.
           GO TO LEER-PRIMEROS.

       PSYS-ERR.
           DISPLAY BLANK-SCREEN.

           PERFORM IMPRIMIR-CABECERA THRU IMPRIMIR-CABECERA.
           CLOSE F-MOVIMIENTOS.
           DISPLAY "Ha ocurrido un error interno" AT LINE 9 COL 25
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Vuelva mas tarde" AT LINE 11 COL 32
               WITH FOREGROUND-COLOR IS WHITE
                    BACKGROUND-COLOR IS RED.
           DISPLAY "Enter - Aceptar" AT LINE 24 COL 33.

        *>    DISPLAY MOV-NUM             AT LINE 13 COL 30.

        *>    DISPLAY MOV-TARJETA         AT LINE 15 COL 30.

        *>    DISPLAY MOV-SALDOPOS-ENT    AT LINE 17 COL 30.

        *>    DISPLAY LAST-MOV-NUM        AT LINE 19 COL 30.

        *>    DISPLAY INCREMENTO          AT LINE 21 COL 30.

        *>    DISPLAY PROG-ORIGEN         AT LINE 23 COL 30.
           
       EXIT-ENTER.
           ACCEPT PRESSED-KEY AT LINE 24 COL 80
           IF ENTER-PRESSED
               EXIT PROGRAM
           ELSE
               GO TO EXIT-ENTER.

       *> Filtrado por fechas
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

       FIN-PROGRAMADAS.
           CLOSE F-PROGRAMADAS.
           CLOSE F-MOVIMIENTOS.
