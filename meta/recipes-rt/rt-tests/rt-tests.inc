# Version v0.85
SRCREV = "210e625227d21e0b589b8eaa36cd81f1cb981819"

PR = "r0"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/clrkwllms/rt-tests.git \
           file://0001-rt-tests-Allow-for-user-specified-PYLIB.patch \
           file://0002-rt-tests-Break-out-install_hwlatdetect.patch \
           file://added-missing-dependencies.patch \
           file://cyclictest-finish-removal-of-1-second-first-loops.patch \
"

INC_PR = "r2"

S = "${WORKDIR}/git"

# need to append rt-tests' default CFLAGS to ours
CFLAGS += "-I${S}/src/include -D_GNU_SOURCE -Wall -Wno-nonnulli"

# calling 'uname -m' is broken on crossbuilds
EXTRA_OEMAKE = "NUMA=0"
