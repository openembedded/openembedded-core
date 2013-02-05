OE_TERMINAL ?= 'auto'
OE_TERMINAL[type] = 'choice'
OE_TERMINAL[choices] = 'auto none \
                        ${@" ".join(o.name \
                                    for o in oe.terminal.prioritized())}'

OE_TERMINAL_EXPORTS += 'EXTRA_OEMAKE'
OE_TERMINAL_EXPORTS[type] = 'list'

XAUTHORITY ?= "${HOME}/.Xauthority"
SHELL ?= "bash"


def oe_terminal(command, title, d):
    import oe.data
    import oe.terminal

    env = dict()

    for v in os.environ:
        env[v] = os.environ[v]

    for export in oe.data.typed_value('OE_TERMINAL_EXPORTS', d):
        value = d.getVar(export, True)
        if value is not None:
            os.environ[export] = str(value)
            env[export] = str(value)

    # Add in all variables from the user's original environment which
    # haven't subsequntly been set/changed
    origbbenv = d.getVar("BB_ORIGENV", False) or {}
    for key in origbbenv:
        if key in env:
            continue
        value = origbbenv.getVar(key, True)
        if value is not None:
            os.environ[key] = str(value)
            env[key] = str(value)

    terminal = oe.data.typed_value('OE_TERMINAL', d).lower()
    if terminal == 'none':
        bb.fatal('Devshell usage disabled with OE_TERMINAL')
    elif terminal != 'auto':
        try:
            oe.terminal.spawn(terminal, command, title, env, d)
            return
        except oe.terminal.UnsupportedTerminal:
            bb.warn('Unsupported terminal "%s", defaulting to "auto"' %
                    terminal)
        except oe.terminal.ExecutionError as exc:
            bb.fatal('Unable to spawn terminal %s: %s' % (terminal, exc))

    try:
        oe.terminal.spawn_preferred(command, title, env, d)
    except oe.terminal.NoSupportedTerminals:
        bb.fatal('No valid terminal found, unable to open devshell')
    except oe.terminal.ExecutionError as exc:
        bb.fatal('Unable to spawn terminal %s: %s' % (terminal, exc))
