# Version v0.94
PV = "0.94"
SRCREV = "a8fd81efa718ec449d90cba8f41123ed521bd12b"

SRC_URI = "git://git.kernel.org/pub/scm/utils/rt-tests/rt-tests.git"

S = "${WORKDIR}/git"

# need to append rt-tests' default CFLAGS to ours
CFLAGS += "-I${S}/src/include -D_GNU_SOURCE -Wall -Wno-nonnull"

# calling 'uname -m' is broken on crossbuilds
EXTRA_OEMAKE = "NUMA=0"
