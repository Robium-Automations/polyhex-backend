#  docker build -t polyhex_db .

FROM postgres:9.3
ENV POSTGRES_USER postgres
ENV POSTGRES_PASSWORD postgres
ENV POSTGRES_DB polyhex_db

# Adjust PostgreSQL configuration so that remote connections to the
RUN echo "host all all all $POSTGRES_HOST_AUTH_METHOD" >> /var/lib/postgresql/data/postgresql.conf

EXPOSE 5432
COPY init.sql /docker-entrypoint-initdb.d/