# Version v0.89
PV = "0.89"
SRCREV = "9164ec4db2645f52ffdd20e6d1b72bb0411ab40e"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/clrkwllms/rt-tests.git \
           file://0001-rt-tests-Allow-for-user-specified-PYLIB.patch \
           file://0002-rt-tests-Break-out-install_hwlatdetect.patch \
           file://added-missing-dependencies.patch \
           file://hackbench-fix-error-check.patch \
"

S = "${WORKDIR}/git"

# need to append rt-tests' default CFLAGS to ours
CFLAGS += "-I${S}/src/include -D_GNU_SOURCE -Wall -Wno-nonnull"

# calling 'uname -m' is broken on crossbuilds
EXTRA_OEMAKE = "NUMA=0"
