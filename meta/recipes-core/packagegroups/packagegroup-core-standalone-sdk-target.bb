SUMMARY = "Target packages for the standalone SDK"
PR = "r8"
LICENSE = "MIT"

inherit packagegroup

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-standalone-sdk-target"
RREPLACES_${PN} = "task-core-standalone-sdk-target"
RCONFLICTS_${PN} = "task-core-standalone-sdk-target"
RPROVIDES_${PN}-dbg = "task-core-standalone-sdk-target-dbg"
RREPLACES_${PN}-dbg = "task-core-standalone-sdk-target-dbg"
RCONFLICTS_${PN}-dbg = "task-core-standalone-sdk-target-dbg"

RDEPENDS_${PN} = "\
    libgcc \
    libgcc-dev \
    libstdc++ \
    libstdc++-dev \
    ${LIBC_DEPENDENCIES} \
    qemuwrapper-cross \
    "
