require opkg.inc

SRC_URI = "svn://opkg.googlecode.com/svn;module=trunk;protocol=http \
  file://0001-add-opkg_compare_versions-function.patch \
  file://0002-Ensure-we-use-the-uname-gname-fields-when-extracting.patch \
  file://0003-Fix-dependency-issues-for-preinst-scripts.patch \
  file://0004-Failed-postinst-script-is-not-fatal-with-conf-offlin.patch \
  file://0005-Do-not-read-etc-opkg-.conf-if-f-is-specified.patch \
  file://0006-detect-circular-dependencies.patch \
  file://0007-merge-newpkg-provides-even-when-oldpkg-provides-exis.patch \
  file://0008-select_higher_version.patch \
  file://0009-pkg_depends-fix-version-constraints.patch \
  file://0010-pkg_depends-fix-version_constraints_satisfied.patch \
"

S = "${WORKDIR}/trunk"

SRCREV = "633"
PV = "0.1.8+svnr${SRCPV}"

PR = "${INC_PR}.5"
