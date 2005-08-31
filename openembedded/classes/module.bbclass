RDEPENDS += "kernel (${KERNEL_VERSION})"
DEPENDS += "virtual/kernel"

inherit module-base

python populate_packages_prepend() {
	v = bb.data.getVar("PARALLEL_INSTALL_MODULES", d, 1) or "0"
	if v == "1":
		kv = bb.data.getVar("KERNEL_VERSION", d, 1)
		packages = bb.data.getVar("PACKAGES", d, 1)
		for p in packages.split():
			pkg = bb.data.getVar("PKG_%s" % p, d, 1) or p
			newpkg = "%s-%s" % (pkg, kv)
			bb.data.setVar("PKG_%s" % p, newpkg, d)
			rprovides = bb.data.getVar("RPROVIDES_%s" % p, d, 1)
			if rprovides:
				rprovides = "%s %s" % (rprovides, pkg)
			else:
				rprovides = pkg
			bb.data.setVar("RPROVIDES_%s" % p, rprovides, d)
}

module_do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake KERNEL_PATH=${STAGING_KERNEL_DIR}   \
		   KERNEL_SRC=${STAGING_KERNEL_DIR}    \
		   KERNEL_VERSION=${KERNEL_VERSION}    \
		   CC="${KERNEL_CC}" LD="${KERNEL_LD}" \
		   ${MAKE_TARGETS}
}

module_do_install() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" CC="${KERNEL_CC}" LD="${KERNEL_LD}" modules_install
}

pkg_postinst_append () {
	if [ -n "$D" ]; then
		exit 1
	fi
	depmod -A
	update-modules || true
}

pkg_postrm_append () {
	update-modules || true
}

EXPORT_FUNCTIONS do_compile do_install

FILES_${PN} = "/etc /lib/modules"
