SUMMARY = "OpenSSH SSH client/server"
LICENSE = "MIT"
PR = "r0"

inherit packagegroup

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-ssh-openssh"

RDEPENDS_${PN} = "openssh"
