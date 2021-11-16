<CsoundSynthesizer>
  <CsOptions>
  -m3 -fRW -odac
  </CsOptions>
  <CsInstruments>
  ; Copyright (c) 2007 by Michael Gogins. All rights reserved.
  ; Inspired by the compositions of LaMonte Young.
  #define DEST  #"/fingerplay/control/1"#
  #define DEST1 #"/fingerplay/control/2"#
  #define DEST2 #"/fingerplay/control/4"#
  #define DEST3 #"/fingerplay/control/5"#
  sr               =           48000
  ksmps            =           10
  nchnls           =           2
  0dbfs                 =             20000
  gareverb1             init          0
  gareverb2             init          0
  gilisten              OSCinit       12345
                   turnon        2
                   instr         1
  iattack          init      20
  idecay          init      20
  p3                    =             p3 + (iattack + idecay) / 2.0
  isustain              =             p3 - (iattack + idecay)
  ifundamental          =             p4
  inumerator            =             p5
  idenominator          =             p6
  ivelocity             =             p7
  ipan                  =             p8
  iratio                =             inumerator / idenominator
  ihertz                =             ifundamental * iratio
  iamp                =             ampdb(ivelocity)
  kdistortfactor        init          0
  next1:
  kans1                 OSClisten     gilisten, $DEST, "i", kdistortfactor
                 if (kans1 == 0) goto done1
  kdistortfactor        =             kdistortfactor / 127.0             
  ;                      printks      "kdistortfactor = %f\\n", 0, kdistortfactor
                   kgoto         next1  ;Process all events in queue
  done1:
  kenvelope              transeg       0.0, iattack, 0.0, iamp, isustain, 0.0, iamp, idecay, 0.0, 0.0
  asignal          poscil3       kenvelope, ihertz, 1
  asignal               distort       asignal, kdistortfactor, 2
  aleft, aright         pan2          asignal , ipan
  gareverb1             =             gareverb1 + aleft
  gareverb2             =             gareverb1 + aright
                   endin
                   instr         2
  kharmonic             init          0
  next3:
  kans3                 OSClisten     gilisten, $DEST2, "i", kharmonic
                 if (kans3 == 0) goto done3
  kharmonic             =             2.0 * kharmonic / 127.0             
  ;                      printks      "kharmonic = %f\\n", 0, kharmonic
                   event         "f", 1, 0, 8193, 10, 3, 0, kharmonic, 0, 0, kharmonic * 2.0
                   kgoto         next3  ;Process all events in queue
  done3:
  kdisttable            init          0
  next4:
  kans4                 OSClisten     gilisten, $DEST3, "i", kdisttable
                 if (kans4 == 0) goto done4
  kdisttable            =             10.0 * kdisttable / 127.0             
  ;                      printks      "kdisttable = %f\\n", 0, kdisttable
                   event         "f", 2, 0, 8193, 13, 1, kdisttable, 0, kdisttable * 2.0, 0, kdisttable * 3.0
                   kgoto         next4  ;Process all events in queue
  done4:
              endin
              instr         3
  kfeedback             init          0
  next2:
  kans2                 OSClisten     gilisten, $DEST1, "i", kfeedback
                 if (kans2 == 0) goto done2
  kfeedback             =             kfeedback / 127.0             
  ;                      printks      "kfeedback = %f\\n", 0, kfeedback
                   kgoto         next2  ;Process all events in queue
  done2:
  aleft, aright         reverbsc      gareverb1, gareverb2, kfeedback, 15000.0
                   outs          gareverb1 + aleft * 0.8, gareverb2 + aright * 0.8
  gareverb1             =             0
  gareverb2             =             0
                   endin
  </CsInstruments>
  <CsScore>
  ;                       A few harmonics...
  ;f   1   0       65536     10      3   0   0   1   0   2
  f   1   0       8193         10    3   0   1   0   0   2
  ;                       ...distorted by waveshaping.
  ;f   2   0        65536          13    1   1   0   3   0   2    
  f   2   0       8193         13    1   1   0   2   0   3
  t   0   4
  ; p1     p2     p3        p4           p5         p6           p7        p8
  ; insno  onset  duration  fundamental  numerator  denominator  velocity  pan
  ;                                      Fundamental
  i 1      0      60        60           1          1            60        0.75
  ;                                      Major whole tone
  i 1      0      60        60           9          8            60        0.25
  ;                                      Septimal octave
  i 1      0      60        60           63         32           60        0.00
  ;                                      Fundamental
  i 1       60     60        53.5546875   1          1            63        0.75
  ;                                      Perfect fifth
  i 1       60     60        53.5546875   3          2            62        0.25
  ;                                      Harmonic seventh
  i 1      60     60        53.5546875   7          4            61        0.00
  ;                                      Fundamental
  i 1      120    60        60           1          1            60        0.75
  ;                                      Major whole tone
  i 1      120    60        60           9          8            60        0.25
  ;                                      Septimal octave
  i 1      120    60        60           63         32           60        0.875
  ;                                      Septimal octave
  i 1      120    60        60           32         63           60        0.125
  i 3      0    -1
  s 10.0
  e 10.0
  </CsScore>
  </CsoundSynthesizer>
<bsbPanel>
 <label>Widgets</label>
 <objectName/>
 <x>100</x>
 <y>100</y>
 <width>320</width>
 <height>240</height>
 <visible>true</visible>
 <uuid/>
 <bgcolor mode="nobackground">
  <r>255</r>
  <g>255</g>
  <b>255</b>
 </bgcolor>
</bsbPanel>
<bsbPresets>
</bsbPresets>
