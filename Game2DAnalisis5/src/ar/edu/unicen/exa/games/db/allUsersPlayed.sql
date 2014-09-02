SELECT DISTINCT pr.username AS username
FROM   		profilegame pr
INNER JOIN	game ga
ON			pr.id_game 			= ga.id 
INNER JOIN	game_gamecategory ggc
ON			ggc.id_game 		= ga.id
INNER JOIN	gamecategory gc
ON			ggc.id_gamecategory = gc.id
INNER JOIN	user u
ON			u.username = pr.username
WHERE		gc.code <> 'other' 
			AND u.userGroup not in ('analia')
--			AND u.username not in ('eferrante','eiarussi')

--			31 neutros y 32 sensitivos segun lista obtenida a partir de la clasificacion usando bayes de todos los jugadores clasificados correctamente						
--			AND u.username in ('246688','246407','246491','247350','247493','247061','246993','247338','nfagotti','247325','244530','246810','246856','247499','247586','jsebey','246888','247465','247159','247074','247692','247139','247667','247823','ffreiberger','247140','aflematti','247070','246750','247498','247819','psuzuki','246916','247787','246906','247381','247637','247183','246921','247420','247412','ribanez','247191','nvillareal','247025','247314','msanchez','247351','246483','sbaisi','246730','247059','jbais','247190','247263','fdefelippe','gcurra','247762','246667','246435','246746','246632','247441','247394','javellaneda')

--			20 intuitivos - 28 neutros - 25 sensitivos			
--			AND u.username in ('eferrante','eiarussi','247192','jluna','amonteserin','247342','lmarquez','hmanterola','dlopez','247440','arodriguez','tpignede','247378','mrossi','247632','246658','247102','mgiamberardino','247239','cvaldezgandara',   '246407','246491','247350','247061','247338','nfagotti','247325','244530','246810','246856','247499','jsebey','247465','247159','247074','247692','247139','247667','247823','ffreiberger','247140','aflematti','247070','246750','247498','247819','psuzuki','246916','247787','246906','247381','247637','247183','247420','247412','ribanez','247191','nvillareal','247025','247314','msanchez','246483','sbaisi','246730','jbais','247190','fdefelippe','gcurra','247762','246435','247441','247394','javellaneda')

--			20 intuitivos - 25 neutros - 23 sensitivos			
--			AND u.username in ('eferrante','eiarussi','247192','jluna','amonteserin','247342','lmarquez','hmanterola','dlopez','247440','arodriguez','tpignede','247378','mrossi','247632','246658','247102','mgiamberardino','247239','cvaldezgandara',   '246407','246491','247350','247061','247338','nfagotti','247325','244530','246810','246856','247499','jsebey','247159','247074','247692','247139','247667','ffreiberger','247140','aflematti','247070','246750','247819','psuzuki','246916','247787','246906','247381','247637','247183','247420','247412','ribanez','247191','nvillareal','247025','247314','msanchez','246483','sbaisi','jbais','247190','fdefelippe','gcurra','247762','246435','247441','javellaneda')
			
ORDER BY u.perception

/* 
	Lista de jugadores clasificados correctamente

// intutivos
jluna
amonteserin
247342
lmarquez
hmanterola
dlopez
eferrante
247440
eiarussi
arodriguez
tpignede
247378
mrossi
247632
246658
247102
mgiamberardino
247239
cvaldezgandara
247192

// neutrales
246688
246407
246491
247350
247493
247061
246993
247338
nfagotti
247325
244530
246810
246856
247499
247586
jsebey
246888
247465
247159
247074
247692
247139
247667
247823
ffreiberger
247140
aflematti
247070
246750
247498
247819
psuzuki
246916

// sensitivos
247787
246906
247381
247637
247183
246921
247420
247412
ribanez
247191
nvillareal
247025
247314
msanchez
247351
246483
sbaisi
246730
247059
jbais
247190
247263
fdefelippe
gcurra
247762
246667
246435
246746
246632
247441
247394
javellaneda
*/