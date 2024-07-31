FROM postgres:15.5-alpine3.19

EXPOSE 5432

ENV POSTGRES_USER admin
ENV POSTGRES_PASSWORD admin
ENV POSTGRES_DB CRM

CMD ["postgres", "-c", "log_statement=all"]