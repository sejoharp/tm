# worktimers

tool to notify players in a tournament.

## Usage

`java -Djava.awt.headless=true -server -Xms48m -Xmx250m -XX:MaxPermSize=150m -jar tm.jar`

## build

`./gradlew build`

## Installation

### install java

### add config

#### tournament.json

Add playsers.

- The name has to match the displayed name in the tournament.
- The chatId is the id from the telegram account. Here are some ways to do
  it: https://stackoverflow.com/questions/32423837/telegram-bot-how-to-get-a-group-chat-id

```json
{
  "players": [
    {
      "name": "User1",
      "chatId": "1"
    },
    {
      "name": "User2",
      "chatId": "2"
    }
  ]
}
```

#### application.yml

adjust the following config

```yaml
tournamentConfigPath: [ path to file ]/tournament.json
telegramUrl: https://api.telegram.org/bot[API KEY]
```

## Development

### create a release

1. create a commit with all changes
1. tag the commit `git tag v1.0.4`
1. `git push && git push --tags`
