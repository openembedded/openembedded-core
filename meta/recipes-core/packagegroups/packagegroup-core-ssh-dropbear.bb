SUMMARY = "Dropbear SSH client/server"
LICENSE = "MIT"
PR = "r1"

inherit packagegroup

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-ssh-dropbear"
RREPLACES_${PN} = "task-core-ssh-dropbear"
RCONFLICTS_${PN} = "task-core-ssh-dropbear"

RDEPENDS_${PN} = "dropbear"
