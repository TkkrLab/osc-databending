<CsoundSynthesizer>
  <CsOptions>
  -m3 -fRW -odac
  </CsOptions>
  <CsInstruments>
  ; Copyright (c) 2007 by Michael Gogins. All rights reserved.
  ; Inspired by the compositions of LaMonte Young.
  #define NOTEON  #"/fingerplay/noteon"#
  #define NOTEOFF #"/fingerplay/noteoff"#
  sr               =           48000
  ksmps            =           10
  nchnls           =           2
  0dbfs            =             20000
  gilisten         OSCinit       12345
  
  gisin	         ftgen         1, 0, 16384, 10, 1
  ;Define scale tuning
  giji_12          ftgen     202, 0, 32, -2, 12, 2, 256, 60, 1, 16/15, 9/8, 6/5, 5/4, 4/3, 7/5, \
                             3/2, 8/5, 5/3, 9/5, 15/8, 2
                   turnon        1
                   turnon        2
                                              
                   instr         1
  knote            init          0
  kvel             init          0
  next1:
  kans1            OSClisten     gilisten, $NOTEON, "ii", knote, kvel
                   if (kans1 == 0) goto done1
                   printks      "noteon: knote = %d, kvel = %d\\n", 0, knote, kvel
  knote            =             knote + 24                   
  kcps             cpstun        kvel, knote, giji_12
  kamp             =             kvel/127
                   printks      "noteon: kcps = %f, kamp = %f\\n", 0, kcps, kamp
                   if (kvel == 0) then
                   turnoff2      1001, 2, 1
                   elseif (kvel > 0) then
                   event         "i", 1001, 0, -1, kcps, kamp
                   endif
                   kgoto         next1  ;Process all events in queue
  done1:
                   endin
                   
                   instr         2
  knote            init          0
  kvel             init          0
  next2:
  kans2            OSClisten     gilisten, $NOTEOFF, "ii", knote, kvel
                   if (kans2 == 0) goto done2
                   printks      "noteoff: knote = %d, kvel = %d\\n", 0, knote, kvel
                   turnoff2      1001, 2, 1
                   kgoto         next2  ;Process all events in queue
  done2:
                   endin
                   instr          1001   ;Simple instrument
  icps	         init           p4
  
  aenv             linsegr        0, .003, p5, 0.03, p5 * 0.5, 0.3, 0
  aosc	         oscil          5000 * aenv, icps, gisin

	              out            aosc
                   endin
  </CsInstruments>
  <CsScore>
  f 0 3600  ;Dummy f-table
  i 1001 0 60 60 1
  e
  </CsScore>
  </CsoundSynthesizer>
<bsbPanel>
 <label>Widgets</label>
 <objectName/>
 <x>0</x>
 <y>0</y>
 <width>0</width>
 <height>0</height>
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
