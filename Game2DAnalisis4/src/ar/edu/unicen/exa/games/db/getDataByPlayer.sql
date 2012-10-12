-- Un registro por jugador
SELECT 	u.username,
		MAX(timesPlayed) AS timesPlayed,
		MAX(level) AS level,
		SUM(correctanswers) AS correctAnswers,
		SUM(incorrectanswers) AS incorrectAnswers,
		SUM(movs) AS movs,
		SUM(movs_in) AS movsin,
		SUM(movs_out) AS movsout,
		SUM(timeout) AS timeout,
		AVG(time) AS timePlayed,
       	SUM(
			CASE
				WHEN won = 'LEVEL_WON' THEN 1
				WHEN won = 'LEVEL_LOST' THEN -1
				ELSE 0
			END
		) AS result,
       	SUM(
			CASE
				WHEN won = 'LEVEL_WON' THEN 1
				ELSE 0
			END
		) AS resultWon,
       	SUM(
			CASE
				WHEN won = 'LEVEL_LOST' THEN 1
				ELSE 0
			END
		) AS resultLost,
	    u.:felderDimension AS class

FROM   	profilegame p
	   	INNER JOIN user u
		ON p.username = u.username

WHERE  	won != 'NEW'
	   	AND won != 'ABANDON'
	   	AND p.id_game = ':game'
	   	AND u.:felderDimension != 0
--	   	AND u.usergroup != 'ProgExpl2012'
		
GROUP BY username
ORDER BY username