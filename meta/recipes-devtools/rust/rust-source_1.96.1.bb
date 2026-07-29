# rust-source_${PN}.bb - Shared source recipe for the Rust toolchain
#
# This recipe fetches, unpacks, and patches the rustc source tree into a
# work-shared location so that rust, cargo, libstd-rs, and their native/nativesdk
# variants all share a single copy of the source. This eliminates duplication
# (up to 11 copies of the ~4.2 GB source tree in multilib + nativesdk builds)
# and significantly reduces disk usage (~46.2 GB -> ~4.2 GB for rustc-src).
#
# Only do_fetch, do_unpack, and do_patch are meaningful here. All other
# tasks are disabled since this recipe exists solely to provide sources.

SUMMARY = "Rust sources"
HOMEPAGE = "http://www.rust-lang.org"
SECTION = "devel"
LICENSE = "Unicode-3.0 AND (Apache-2.0 OR MIT)"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=11a3899825f4376896e438c8c753f8dc"

# This recipe only provides source; disable build/install/packaging tasks
deltask do_configure
deltask do_compile
deltask do_install
deltask do_populate_sysroot
deltask do_populate_lic

# Prevent rm_work from removing the shared source tree
RM_WORK_EXCLUDE += "${PN}"

require rust-source.inc
require rust-snapshot.inc

inherit allarch nopackages

PN = "rust-source-${PV}"
BPN = "rust-source"

# Place WORKDIR under work-shared so all consumers reference the same location
WORKDIR = "${TMPDIR}/work-shared/rust-source-${PV}-${PR}"
SSTATE_SWSPEC = "sstate:rust-source::${PV}:${PR}::${SSTATE_VERSION}:"

# Use work-shared stamp directory so sstate is shared across all architectures
STAMP = "${STAMPS_DIR}/work-shared/rust-source-${PV}-${PR}"
STAMPCLEAN = "${STAMPS_DIR}/work-shared/rust-source-${PV}-*"

DEPENDS = ""
PACKAGES = ""
baselib = "lib"
PACKAGE_ARCH = "all"

B = "${WORKDIR}/build"

# rust-source.bb does not inherit rust-common.bbclass (it is allarch/nopackages
# and has no toolchain dependencies), so we define RUST_BUILD_ARCH locally for
# snapshot URL resolution in rust-snapshot.inc.
RUST_BUILD_ARCH = "${@{'ppc': 'powerpc', 'ppc64': 'powerpc64', 'ppc64le': 'powerpc64le', 'riscv64': 'riscv64gc'}.get(d.getVar('BUILD_ARCH'), d.getVar('BUILD_ARCH'))}"

EXCLUDE_FROM_WORLD = "1"
