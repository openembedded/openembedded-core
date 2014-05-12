SRCREV = "7055ffd37beeb44714e86a4abc703f7e175a0db5"
PR = "r3"
PV = "1.2+git${SRCPV}"

inherit pkgconfig pythonnative

FILESEXTRAPATHS =. "${FILE_DIRNAME}/trace-cmd:"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/rostedt/trace-cmd.git \
           file://addldflags.patch \
           file://make-docs-optional.patch \
           file://blktrace-api-compatibility.patch \
"
S = "${WORKDIR}/git"
