project = aka

all: build

build:
	docker-compose run --no-deps ${project} uberjar
	docker build -t cncflora/${project} .
	sudo chown ${USER}.${USER} * -Rf
	sudo chown ${USER}.${USER} ~/.m2 -Rf

push:
	docker push cncflora/${project}

test: 
	docker-compose run -t ${project} test

run:
	docker-compose up
	sudo chown ${USER}.${USER} * -Rf
	sudo chown ${USER}.${USER} ~/.m2 -Rf

start:
	docker-compose up -d

stop:
	docker-compose stop
	sudo chown ${USER}.${USER} * -Rf
	sudo chown ${USER}.${USER} ~/.m2 -Rf

fix:
	sudo chown ${USER}.${USER} * -R
	sudo chown ${USER}.${USER} ~/.m2 -R

