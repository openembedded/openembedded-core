# Version v0.91
PV = "0.91"
SRCREV = "3fed00ff8d2c0cdbc5ba078cbd60ce3cfcee615f"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/clrkwllms/rt-tests.git \
           file://0001-rt-tests-Allow-for-user-specified-PYLIB.patch \
           file://0002-rt-tests-Break-out-install_hwlatdetect.patch \
           file://added-missing-dependencies.patch \
           file://hackbench-fix-error-check.patch \
           file://Makefile-fix-gzip-command.patch \
"

S = "${WORKDIR}/git"

# need to append rt-tests' default CFLAGS to ours
CFLAGS += "-I${S}/src/include -D_GNU_SOURCE -Wall -Wno-nonnull"

# calling 'uname -m' is broken on crossbuilds
EXTRA_OEMAKE = "NUMA=0"
