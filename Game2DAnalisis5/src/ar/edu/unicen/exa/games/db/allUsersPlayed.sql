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
--			AND u.userGroup like 'progexpl2013'
ORDER BY pr.username