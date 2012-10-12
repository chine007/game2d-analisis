-- Un registro por accion del jugador
SELECT 	u.username,
		timesPlayed AS timesPlayed,
		level AS level,
		correctanswers AS correctAnswers,
		incorrectanswers AS incorrectAnswers,
		time AS timePlayed,
		CASE
				WHEN won = 'LEVEL_WON' THEN 1
				WHEN won = 'LEVEL_LOST' THEN -1
				ELSE 0
		END AS result,
		CASE
				WHEN won = 'LEVEL_WON' THEN 1
				ELSE 0
		END AS resultWon,
		CASE
				WHEN won = 'LEVEL_LOST' THEN 1
				ELSE 0
		END AS resultLost,
	    u.:felderDimension AS class

FROM   	profilegame p
	   	INNER JOIN user u
		ON p.username = u.username

WHERE  	won != 'NEW'
	   	AND won != 'ABANDON'
	   	AND p.id_game = ':game'
	   	AND u.:felderDimension != 0
		
ORDER BY username, trace, level 	