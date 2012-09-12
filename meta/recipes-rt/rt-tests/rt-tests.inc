# Version v0.84
SRCREV = "857cdd5320ce1f293f5dbcbec79cc8fe22b0bebf"

PR = "r0"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/clrkwllms/rt-tests.git \
           file://0001-rt-tests-Allow-for-user-specified-PYLIB.patch \
           file://0002-rt-tests-Break-out-install_hwlatdetect.patch"

INC_PR = "r0"

S = "${WORKDIR}/git"

# need to append rt-tests' default CFLAGS to ours
CFLAGS += "-I${S}/src/include -D_GNU_SOURCE -Wall -Wno-nonnulli"

# calling 'uname -m' is broken on crossbuilds
EXTRA_OEMAKE = "NUMA=0"
