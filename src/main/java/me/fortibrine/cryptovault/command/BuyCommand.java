package me.fortibrine.cryptovault.command;

import dev.rollczi.litecommands.annotations.argument.Arg;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.annotations.execute.Execute;

@Command(name = "cryptovault", aliases = {"cv"})
public class BuyCommand {

    @Execute(name = "buy")
    public void execute(@Arg double amount) {

    }

}
