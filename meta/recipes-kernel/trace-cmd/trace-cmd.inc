SRCREV = "57371aaa2f469d0ba15fd85276deca7bfdd7ce36"
PV = "2.6.2"

inherit pkgconfig

FILESEXTRAPATHS =. "${FILE_DIRNAME}/trace-cmd:"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/rostedt/trace-cmd.git;branch=trace-cmd-stable-v2.6 \
           file://blktrace-api-compatibility.patch \
           file://0001-Include-limits.h-so-that-PATH_MAX-is-defined-an-issu.patch \
"

S = "${WORKDIR}/git"
