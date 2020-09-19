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

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Mark Vainomaa
 */
public final class ServerCommand implements SimpleCommand {
    private final VelocitySlashServer plugin;
    private final String serverName;
    private final String permissionNode;

    ServerCommand(VelocitySlashServer plugin, String serverName) {
        this.plugin = plugin;
        this.serverName = serverName;
        this.permissionNode = "velocity.server" + serverName;
    }

    @NonNull
    String getServerName() {
        return this.serverName;
    }

    @Override
    public void execute(Invocation invocation) {
        var source = invocation.source();
        if (!(source instanceof Player)) {
            // TODO: message "must be a player"
            return;
        }

        Player player = (Player) source;

        // Get the server by name
        RegisteredServer server = this.plugin.getServer().getServer(this.getServerName())
                .orElseThrow(() -> new IllegalStateException("Server '" + this.getServerName() + "' does not exist"));

        player.createConnectionRequest(server).fireAndForget();
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission(permissionNode);
    }
}
