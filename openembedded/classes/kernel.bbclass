inherit module_strip

PROVIDES += "virtual/kernel"
DEPENDS += "virtual/${TARGET_PREFIX}depmod-${@get_kernelmajorversion('${PV}')} virtual/${TARGET_PREFIX}gcc${KERNEL_CCSUFFIX} update-modules"

inherit kernel-arch

export OS = "${TARGET_OS}"
export CROSS_COMPILE = "${TARGET_PREFIX}"
KERNEL_IMAGETYPE = "zImage"

KERNEL_PRIORITY = "${@bb.data.getVar('PV',d,1).split('-')[0].split('.')[-1]}"

KERNEL_CCSUFFIX ?= ""
KERNEL_LDSUFFIX ?= ""

KERNEL_CC = "${CCACHE}${HOST_PREFIX}gcc${KERNEL_CCSUFFIX}"
KERNEL_LD = "${LD}${KERNEL_LDSUFFIX}"

KERNEL_OUTPUT = "arch/${ARCH}/boot/${KERNEL_IMAGETYPE}"
KERNEL_IMAGEDEST = "boot"

#
# configuration
#
export CMDLINE_CONSOLE = "console=${@bb.data.getVar("KERNEL_CONSOLE",d,1) or "ttyS0"}"

# parse kernel ABI version out of <linux/version.h>
def get_kernelversion(p):
	import re
	try:
		f = open(p, 'r')
	except IOError:
		return None
	l = f.readlines()
	f.close()
	r = re.compile("#define UTS_RELEASE \"(.*)\"")
	for s in l:
		m = r.match(s)
		if m:
			return m.group(1)
	return None

def get_kernelmajorversion(p):
	import re
	r = re.compile("([0-9]+\.[0-9]+).*")
	m = r.match(p);
	if m:
		return m.group(1)
	return None

KERNEL_VERSION = "${@get_kernelversion('${S}/include/linux/version.h')}"
KERNEL_MAJOR_VERSION = "${@get_kernelmajorversion('${KERNEL_VERSION}')}"

KERNEL_LOCALVERSION ?= ""

# kernels are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

kernel_do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	oe_runmake include/linux/version.h CC="${KERNEL_CC}" LD="${KERNEL_LD}"
	oe_runmake dep CC="${KERNEL_CC}" LD="${KERNEL_LD}"
	oe_runmake ${KERNEL_IMAGETYPE} CC="${KERNEL_CC}" LD="${KERNEL_LD}"
	if (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
		oe_runmake modules  CC="${KERNEL_CC}" LD="${KERNEL_LD}"
	else
		oenote "no modules to compile"
	fi
}

kernel_do_stage() {
	ASMDIR=`readlink include/asm`

	mkdir -p ${STAGING_KERNEL_DIR}/include/$ASMDIR
	cp -fR include/$ASMDIR/* ${STAGING_KERNEL_DIR}/include/$ASMDIR/
	ln -sf $ASMDIR ${STAGING_KERNEL_DIR}/include/asm

	mkdir -p ${STAGING_KERNEL_DIR}/include/asm-generic
	cp -fR include/asm-generic/* ${STAGING_KERNEL_DIR}/include/asm-generic/

	mkdir -p ${STAGING_KERNEL_DIR}/include/linux
	cp -fR include/linux/* ${STAGING_KERNEL_DIR}/include/linux/

	mkdir -p ${STAGING_KERNEL_DIR}/include/net
	cp -fR include/net/* ${STAGING_KERNEL_DIR}/include/net/

	mkdir -p ${STAGING_KERNEL_DIR}/include/pcmcia
	cp -fR include/pcmcia/* ${STAGING_KERNEL_DIR}/include/pcmcia/

	if [ -d drivers/sound ]; then
		# 2.4 alsa needs some headers from this directory
		mkdir -p ${STAGING_KERNEL_DIR}/include/drivers/sound
		cp -fR drivers/sound/*.h ${STAGING_KERNEL_DIR}/include/drivers/sound/
	fi

	install -m 0644 .config ${STAGING_KERNEL_DIR}/config-${PV}${KERNEL_LOCALVERSION}
	ln -sf config-${PV}${KERNEL_LOCALVERSION} ${STAGING_KERNEL_DIR}/.config
	ln -sf config-${PV}${KERNEL_LOCALVERSION} ${STAGING_KERNEL_DIR}/kernel-config
	echo "${KERNEL_VERSION}" >${STAGING_KERNEL_DIR}/kernel-abiversion
	echo "${S}" >${STAGING_KERNEL_DIR}/kernel-source
	echo "${KERNEL_CCSUFFIX}" >${STAGING_KERNEL_DIR}/kernel-ccsuffix
	echo "${KERNEL_LDSUFFIX}" >${STAGING_KERNEL_DIR}/kernel-ldsuffix
	[ -e Rules.make ] && install -m 0644 Rules.make ${STAGING_KERNEL_DIR}/
	[ -e Makefile ] && install -m 0644 Makefile ${STAGING_KERNEL_DIR}/
	
	# Check if arch/${ARCH}/Makefile exists and install it
	if [ -e arch/${ARCH}/Makefile ]; then
		install -d ${STAGING_KERNEL_DIR}/arch/${ARCH}
		install -m 0644 arch/${ARCH}/Makefile ${STAGING_KERNEL_DIR}/arch/${ARCH}
	fi
	cp -fR include/config* ${STAGING_KERNEL_DIR}/include/	
	install -m 0644 ${KERNEL_OUTPUT} ${STAGING_KERNEL_DIR}/${KERNEL_IMAGETYPE}
	install -m 0644 System.map ${STAGING_KERNEL_DIR}/System.map-${PV}${KERNEL_LOCALVERSION}
	[ -e Module.symvers ] && install -m 0644 Module.symvers ${STAGING_KERNEL_DIR}/

	cp -fR scripts ${STAGING_KERNEL_DIR}/
}

kernel_do_install() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
	if (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
		oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" modules_install
	else
		oenote "no modules to install"
	fi
	
	install -d ${D}/${KERNEL_IMAGEDEST}
	install -d ${D}/boot
	install -m 0644 ${KERNEL_OUTPUT} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${PV}${KERNEL_LOCALVERSION}
	install -m 0644 System.map ${D}/boot/System.map-${PV}${KERNEL_LOCALVERSION}
	install -m 0644 .config ${D}/boot/config-${PV}${KERNEL_LOCALVERSION}
	install -d ${D}/etc/modutils

        # Check if scripts/genksyms exists and if so, build it
        if [ -e scripts/genksyms/ ]; then
                oe_runmake SUBDIRS="scripts/genksyms"
        fi

        cp -fR scripts ${STAGING_KERNEL_DIR}/
}

kernel_do_configure() {
        yes '' | oe_runmake oldconfig
}

pkg_postinst_kernel () {
	update-alternatives --install /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} ${KERNEL_IMAGETYPE} /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${PV}${KERNEL_LOCALVERSION} ${KERNEL_PRIORITY} || true
}

pkg_postrm_kernel () {
	update-alternatives --remove ${KERNEL_IMAGETYPE} /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${PV}${KERNEL_LOCALVERSION} || true
}

inherit cml1

EXPORT_FUNCTIONS do_compile do_install do_stage do_configure

PACKAGES = "kernel kernel-image kernel-dev"
FILES = ""
FILES_kernel-image = "/boot/${KERNEL_IMAGETYPE}*"
FILES_kernel-dev = "/boot/System.map* /boot/config*"
RDEPENDS_kernel = "kernel-image-${KERNEL_VERSION}"
PKG_kernel-image = "kernel-image-${KERNEL_VERSION}"
ALLOW_EMPTY_kernel = "1"
ALLOW_EMPTY_kernel-image = "1"

pkg_postinst_modules () {
if [ -n "$D" ]; then
	${HOST_PREFIX}depmod -A -b $D -F ${STAGING_KERNEL_DIR}/System.map-${PV}${KERNEL_LOCALVERSION} ${KERNEL_VERSION}
else
	depmod -A
	update-modules || true
fi
}

pkg_postrm_modules () {
update-modules || true
}

autoload_postinst_fragment() {
if [ x"$D" = "x" ]; then
	modprobe %s || true
fi
}

# autoload defaults (alphabetically sorted)
module_autoload_hidp = "hidp"
module_autoload_ipv6 = "ipv6"
module_autoload_ipsec = "ipsec"
module_autoload_ircomm-tty = "ircomm-tty"
module_autoload_rfcomm = "rfcomm"
module_autoload_sa1100-rtc = "sa1100-rtc"

# alias defaults (alphabetically sorted)
module_conf_af_packet = "alias net-pf-17 af_packet"
module_conf_bluez = "alias net-pf-31 bluez"
module_conf_bnep = "alias bt-proto-4 bnep"
module_conf_hci_uart = "alias tty-ldisc-15 hci_uart"
module_conf_l2cap = "alias bt-proto-0 l2cap"
module_conf_sco = "alias bt-proto-2 sco"
module_conf_rfcomm = "alias bt-proto-3 rfcomm"

python populate_packages_prepend () {
	def extract_modinfo(file):
		import os, re
		tmpfile = os.tmpnam()
		cmd = "PATH=\"%s\" %sobjcopy -j .modinfo -O binary %s %s" % (bb.data.getVar("PATH", d, 1), bb.data.getVar("HOST_PREFIX", d, 1) or "", file, tmpfile)
		os.system(cmd)
		f = open(tmpfile)
		l = f.read().split("\000")
		f.close()
		os.unlink(tmpfile)
		exp = re.compile("([^=]+)=(.*)")
		vals = {}
		for i in l:
			m = exp.match(i)
			if not m:
				continue
			vals[m.group(1)] = m.group(2)
		return vals
	
	def parse_depmod():
		import os, re

		dvar = bb.data.getVar('D', d, 1)
		if not dvar:
			bb.error("D not defined")
			return

		kernelver = bb.data.getVar('PV', d, 1) + bb.data.getVar('KERNEL_LOCALVERSION', d, 1)
		kernelver_stripped = kernelver
		m = re.match('^(.*-hh.*)[\.\+].*$', kernelver)
		if m:
			kernelver_stripped = m.group(1)
		path = bb.data.getVar("PATH", d, 1)
		host_prefix = bb.data.getVar("HOST_PREFIX", d, 1) or ""

		cmd = "PATH=\"%s\" %sdepmod -n -a -r -b %s -F %s/boot/System.map-%s %s" % (path, host_prefix, dvar, dvar, kernelver, kernelver_stripped)
		f = os.popen(cmd, 'r')

		deps = {}
		pattern0 = "^(.*\.k?o):..*$"
		pattern1 = "^(.*\.k?o):\s*(.*\.k?o)\s*$"
		pattern2 = "^(.*\.k?o):\s*(.*\.k?o)\s*\\\$"
		pattern3 = "^\t(.*\.k?o)\s*\\\$"
		pattern4 = "^\t(.*\.k?o)\s*$"

		line = f.readline()
		while line:
			if not re.match(pattern0, line):
				line = f.readline()
				continue
			m1 = re.match(pattern1, line)
			if m1:
				deps[m1.group(1)] = m1.group(2).split()
			else:
				m2 = re.match(pattern2, line)
				if m2:
					deps[m2.group(1)] = m2.group(2).split()
					line = f.readline()
					m3 = re.match(pattern3, line)
					while m3:
						deps[m2.group(1)].extend(m3.group(1).split())
						line = f.readline()
						m3 = re.match(pattern3, line)
					m4 = re.match(pattern4, line)
					deps[m2.group(1)].extend(m4.group(1).split())
			line = f.readline()
		f.close()
		return deps
	
	def get_dependencies(file, pattern, format):
		file = file.replace(bb.data.getVar('D', d, 1) or '', '', 1)

		if module_deps.has_key(file):
			import os.path, re
			dependencies = []
			for i in module_deps[file]:
				m = re.match(pattern, os.path.basename(i))
				if not m:
					continue
				on = legitimize_package_name(m.group(1))
				dependency_pkg = format % on
        			v = bb.data.getVar("PARALLEL_INSTALL_MODULES", d, 1) or "0"
	        		if v == "1":
		                	kv = bb.data.getVar("KERNEL_MAJOR_VERSION", d, 1)
					dependency_pkg = "%s-%s" % (dependency_pkg, kv)
				dependencies.append(dependency_pkg)
			return dependencies
		return []

	def frob_metadata(file, pkg, pattern, format, basename):
		import re
		vals = extract_modinfo(file)

		dvar = bb.data.getVar('D', d, 1)

		# If autoloading is requested, output /etc/modutils/<name> and append
		# appropriate modprobe commands to the postinst
		autoload = bb.data.getVar('module_autoload_%s' % basename, d, 1)
		if autoload:
			name = '%s/etc/modutils/%s' % (dvar, basename)
			f = open(name, 'w')
			for m in autoload.split():
				f.write('%s\n' % m)
			f.close()
			postinst = bb.data.getVar('pkg_postinst_%s' % pkg, d, 1)
			if not postinst:
				bb.fatal("pkg_postinst_%s not defined" % pkg)
			postinst += bb.data.getVar('autoload_postinst_fragment', d, 1) % autoload
			bb.data.setVar('pkg_postinst_%s' % pkg, postinst, d)

		# Write out any modconf fragment
		modconf = bb.data.getVar('module_conf_%s' % basename, d, 1)
		if modconf:
			name = '%s/etc/modutils/%s.conf' % (dvar, basename)
			f = open(name, 'w')
			f.write("%s\n" % modconf)
			f.close()

		files = bb.data.getVar('FILES_%s' % pkg, d, 1)
		files = "%s /etc/modutils/%s /etc/modutils/%s.conf" % (files, basename, basename)
		bb.data.setVar('FILES_%s' % pkg, files, d)

		if vals.has_key("description"):
			old_desc = bb.data.getVar('DESCRIPTION_' + pkg, d, 1) or ""
			bb.data.setVar('DESCRIPTION_' + pkg, old_desc + "; " + vals["description"], d)

		rdepends_str = bb.data.getVar('RDEPENDS_' + pkg, d, 1)
		if rdepends_str:
			rdepends = rdepends_str.split()
		else:
			rdepends = []
		rdepends.extend(get_dependencies(file, pattern, format))
		bb.data.setVar('RDEPENDS_' + pkg, ' '.join(rdepends), d)

	module_deps = parse_depmod()
	module_regex = '^(.*)\.k?o$'
	module_pattern = 'kernel-module-%s'

	postinst = bb.data.getVar('pkg_postinst_modules', d, 1)
	postrm = bb.data.getVar('pkg_postrm_modules', d, 1)
	do_split_packages(d, root='/lib/modules', file_regex=module_regex, output_pattern=module_pattern, description='%s kernel module', postinst=postinst, postrm=postrm, recursive=True, hook=frob_metadata, extra_depends='update-modules kernel-image-%s' % bb.data.getVar("KERNEL_VERSION", d, 1))

	import re, os
	metapkg = "kernel-modules"
	bb.data.setVar('ALLOW_EMPTY_' + metapkg, "1", d)
	bb.data.setVar('FILES_' + metapkg, "", d)
	blacklist = [ 'kernel-dev', 'kernel-image' ]
	for l in module_deps.values():
		for i in l:
			pkg = module_pattern % legitimize_package_name(re.match(module_regex, os.path.basename(i)).group(1))
			blacklist.append(pkg)
	metapkg_rdepends = []
	packages = bb.data.getVar('PACKAGES', d, 1).split()
	for pkg in packages[1:]:
		if not pkg in blacklist and not pkg in metapkg_rdepends:
			metapkg_rdepends.append(pkg)
	bb.data.setVar('RDEPENDS_' + metapkg, ' '.join(metapkg_rdepends), d)
	bb.data.setVar('DESCRIPTION_' + metapkg, 'Kernel modules meta package', d)
	packages.append(metapkg)
	bb.data.setVar('PACKAGES', ' '.join(packages), d)

	v = bb.data.getVar("PARALLEL_INSTALL_MODULES", d, 1) or "0"
	if v == "1":
		kv = bb.data.getVar("KERNEL_MAJOR_VERSION", d, 1)
		packages = bb.data.getVar("PACKAGES", d, 1)
		module_re = re.compile("^kernel-module-")
		for p in packages.split():
			if not module_re.match(p):
				continue
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
