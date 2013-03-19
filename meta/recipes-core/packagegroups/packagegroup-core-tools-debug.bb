#
# Copyright (C) 2008 OpenedHand Ltd.
#

SUMMARY = "Debugging tools"
LICENSE = "MIT"

inherit packagegroup

PR = "r2"

PACKAGE_ARCH = "${MACHINE_ARCH}"

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-tools-debug"
RREPLACES_${PN} = "task-core-tools-debug"
RCONFLICTS_${PN} = "task-core-tools-debug"

MTRACE = ""
MTRACE_libc-glibc = "libc-mtrace"

RDEPENDS_${PN} = "\
    gdb \
    gdbserver \
    strace \
    ${MTRACE} \
    "
