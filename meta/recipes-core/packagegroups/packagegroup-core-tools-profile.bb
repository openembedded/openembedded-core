#
# Copyright (C) 2008 OpenedHand Ltd.
#

SUMMARY = "Profiling tools"
LICENSE = "MIT"

PR = "r3"

inherit packagegroup

PACKAGE_ARCH = "${MACHINE_ARCH}"

# For backwards compatibility after rename
RPROVIDES_${PN} = "task-core-tools-profile"
RREPLACES_${PN} = "task-core-tools-profile"
RCONFLICTS_${PN} = "task-core-tools-profile"

PROFILE_TOOLS_X = "${@base_contains('DISTRO_FEATURES', 'x11', 'sysprof', '', d)}"

RRECOMMENDS_${PN} = "\
    perf \
    trace-cmd \
    kernel-module-oprofile \
    blktrace \
    ${PROFILE_TOOLS_X} \
    "

PROFILETOOLS = "\
    oprofile \
    oprofileui-server \
    powertop \
    latencytop \
    "

# systemtap needs elfutils which is not fully buildable on uclibc
# hence we exclude it from uclibc based builds
SYSTEMTAP = "systemtap"
SYSTEMTAP_libc-uclibc = ""
SYSTEMTAP_mips = ""
SYSTEMTAP_aarch64 = ""

# lttng-ust uses sched_getcpu() which is not there on uclibc
# for some of the architectures it can be patched to call the
# syscall directly but for x86_64 __NR_getcpu is a vsyscall
# which means we can not use syscall() to call it. So we ignore
# it for x86_64/uclibc

LTTNGUST = "lttng-ust"
LTTNGUST_libc-uclibc = ""
LTTNGUST_mips = ""
LTTNGUST_aarch64 = ""

# lttng-tools, lttng-modules and babeltrace all depend on liburcu
# which currentl doesn't build on mips

LTTNGTOOLS = "lttng-tools"
LTTNGTOOLS_mips = ""
LTTNGTOOLS_aarch64 = ""

LTTNGMODULES = "lttng-modules"
LTTNGMODULES_mips = ""
LTTNGMODULES_aarch64 = ""

BABELTRACE = "babeltrace"
BABELTRACE_mips = ""
BABELTRACE_aarch64 = ""

# valgrind does not work on mips

VALGRIND = "valgrind"
VALGRIND_libc-uclibc = ""
VALGRIND_mips = ""
VALGRIND_arm = ""
VALGRIND_aarch64 = ""

#    exmap-console
#    exmap-server

# At present we only build lttng-ust on
# qemux86/qemux86-64/qemuppc/qemuarm/emenlow/atom-pc since upstream liburcu
# (which is required by lttng-ust) may not build on other platforms, like
# MIPS.
RDEPENDS_${PN} = "\
    ${PROFILETOOLS} \
    ${LTTNGUST} \
    ${LTTNGTOOLS} \
    ${LTTNGMODULES} \
    ${BABELTRACE} \
    ${SYSTEMTAP} \
    ${VALGRIND} \
    "
