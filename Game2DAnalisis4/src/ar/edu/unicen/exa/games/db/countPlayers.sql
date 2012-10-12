SELECT	
		COUNT(DISTINCT p.username) AS counter
FROM	user u
  		JOIN profilegame p
  		ON p.username = u.username
WHERE
  		p.id_game = ':game'
  		AND u.:felderDimension BETWEEN :from AND :to
  		AND u.:felderDimension != 0