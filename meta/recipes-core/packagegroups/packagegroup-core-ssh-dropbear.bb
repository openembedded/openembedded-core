SUMMARY = "Dropbear SSH client/server"
LICENSE = "MIT"
PR = "r0"

inherit packagegroup

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-ssh-dropbear"

RDEPENDS_${PN} = "dropbear"
