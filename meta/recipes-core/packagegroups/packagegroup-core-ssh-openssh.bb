SUMMARY = "OpenSSH SSH client/server"
LICENSE = "MIT"
PR = "r1"

inherit packagegroup

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-ssh-openssh"
RREPLACES_${PN} = "task-core-ssh-openssh"
RCONFLICTS_${PN} = "task-core-ssh-openssh"

RDEPENDS_${PN} = "openssh"
