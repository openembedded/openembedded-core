inherit terminal

DEVSHELL = "${SHELL}"

python do_devshell () {
    oe_terminal(d.getVar('DEVSHELL', True), 'OpenEmbedded Developer Shell', d)
}

addtask devshell after do_patch

do_devshell[dirs] = "${S}"
do_devshell[nostamp] = "1"

# devshell and fakeroot/pseudo need careful handling since only the final
# command should run under fakeroot emulation, any X connection should
# be done as the normal user. We therfore carefully construct the envionment
# manually
python () {
    if d.getVarFlag("do_devshell", "fakeroot"):
       d.prependVar("DEVSHELL", "pseudo ")
       fakeenv = d.getVar("FAKEROOTENV", True).split()
       for f in fakeenv:
            k = f.split("=")
            d.setVar(k[0], k[1])           
            d.appendVar("OE_TERMINAL_EXPORTS", " " + k[0])
       d.delVarFlag("do_devshell", "fakeroot")
} 
