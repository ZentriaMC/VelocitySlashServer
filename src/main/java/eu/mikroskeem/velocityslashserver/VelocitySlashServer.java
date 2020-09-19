/*
 * This file is part of project SlashServer, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2019-2020 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.velocityslashserver;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mark Vainomaa
 */
@Plugin(id = "%ID%", name = "%NAME%", version = "%VERSION%", description = "%DESCRIPTION%", authors = { "%AUTHORS%" })
public final class VelocitySlashServer {
    @Inject private Logger logger;
    @Inject private ProxyServer server;
    @Inject @DataDirectory private Path dataFolder;

    private final List<ServerCommand> registeredCommands = new LinkedList<>();

    @Subscribe
    public void on(ProxyInitializeEvent event) {
        // TODO: locales

        setupCommands();
    }

    @Subscribe
    public void on(ProxyReloadEvent event) {
        // Re-initialize commands
        setupCommands();
    }

    private void setupCommands() {
        // Unregister previous commands
        registeredCommands.forEach(command -> this.server.getCommandManager().unregister(command.getServerName()));
        registeredCommands.clear();

        // Register new commands
        this.server.getAllServers().stream()
                .map(server -> new ServerCommand(this, server.getServerInfo().getName()))
                .peek(registeredCommands::add)
                .forEach(this::registerCommand);
    }

    private void registerCommand(ServerCommand command) {
        var meta = this.server.getCommandManager().metaBuilder(command.getServerName()).build();
        this.server.getCommandManager().register(meta, command);
    }

    @NonNull
    ProxyServer getServer() {
        return server;
    }
}
