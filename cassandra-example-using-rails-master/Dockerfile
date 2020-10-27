FROM ruby:2.7-alpine

# throw errors if Gemfile has been modified since Gemfile.lock
RUN bundle config --global frozen 1

WORKDIR /usr/src/app

COPY Gemfile Gemfile.lock ./

RUN bundle install --without development test; exit 0

COPY . .

EXPOSE 3000

CMD ["./entrypoint.sh"]