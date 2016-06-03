# Version v0.96
SRCREV = "e1b1537a20b35af75a49bf55dcf70296f8a62467"

SRC_URI = "git://git.kernel.org/pub/scm/utils/rt-tests/rt-tests.git"

S = "${WORKDIR}/git"

# need to append rt-tests' default CFLAGS to ours
CFLAGS += "-I${S}/src/include -D_GNU_SOURCE -Wall -Wno-nonnull"

# calling 'uname -m' is broken on crossbuilds
EXTRA_OEMAKE = "NUMA=0"
