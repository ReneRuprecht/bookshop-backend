fresh:
	make down
	docker-compose build --no-cache

up:
	docker-compose up -d

down:
	docker-compose down
