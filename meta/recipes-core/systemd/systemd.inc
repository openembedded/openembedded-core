SUMMARY = "A System and service manager"
HOMEPAGE = "http://www.freedesktop.org/wiki/Software/systemd"

DESCRIPTION = "systemd is a system and service manager for Linux, compatible with \
SysV and LSB init scripts. systemd provides aggressive parallelization \
capabilities, uses socket and D-Bus activation for starting services, \
offers on-demand starting of daemons, keeps track of processes using \
Linux cgroups, supports snapshotting and restoring of the system \
state, maintains mount and automount points and implements an \
elaborate transactional dependency-based service control logic. It can \
work as a drop-in replacement for sysvinit."

LICENSE = "GPLv2 & LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE.GPL2;md5=751419260aa954499f7abaabaa882bbe \
                    file://LICENSE.LGPL2.1;md5=4fbd65380cdd255951079008b364516c"

# DNSOverTLS strict mode was added in 243
# https://github.com/systemd/systemd/issues/9397
CVE_CHECK_WHITELIST += "CVE-2018-21029"

# Commit dc903ec516cb on the 241 branch
CVE_CHECK_WHITELIST += "CVE-2019-3843 CVE-2019-3844"

SRCREV = "d1cc09a6eac5f8d62e630cc31d604095e30d2d84"
SRCBRANCH = "v241-stable"
SRC_URI = "git://github.com/systemd/systemd-stable.git;protocol=git;branch=${SRCBRANCH}"

S = "${WORKDIR}/git"

PV_append = "+${SRCPV}"
