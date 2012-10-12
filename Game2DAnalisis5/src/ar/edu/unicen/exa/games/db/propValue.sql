SELECT 		:oper(:prop) AS value
FROM   		profilegame pr
INNER JOIN	game ga
ON			pr.id_game 			= ga.id 
INNER JOIN	game_gamecategory ggc
ON			ggc.id_game 		= ga.id
INNER JOIN	gamecategory gc
ON			ggc.id_gamecategory = gc.id
WHERE		pr.username 	= ':username'
AND			gc.code 		= ':category' 		
AND			(pr.won 		= ':won'
OR			':won'			= '')