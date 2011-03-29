inherit terminal

python do_devshell () {
    oe_terminal(d.getVar('SHELL', True), 'OpenEmbedded Developer Shell', d)
}

addtask devshell after do_patch

do_devshell[dirs] = "${S}"
do_devshell[nostamp] = "1"
