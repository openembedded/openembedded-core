inherit linux-kernel-base kernel-module-split

PROVIDES += "virtual/kernel"
DEPENDS += "virtual/${TARGET_PREFIX}binutils virtual/${TARGET_PREFIX}gcc kmod-native depmodwrapper-cross bc-native"

# we include gcc above, we dont need virtual/libc
INHIBIT_DEFAULT_DEPS = "1"

KERNEL_IMAGETYPE ?= "zImage"
INITRAMFS_IMAGE ?= ""
INITRAMFS_TASK ?= ""
INITRAMFS_IMAGE_BUNDLE ?= ""

python __anonymous () {
    kerneltype = d.getVar('KERNEL_IMAGETYPE', True)
    if kerneltype == 'uImage':
        depends = d.getVar("DEPENDS", True)
        depends = "%s u-boot-mkimage-native" % depends
        d.setVar("DEPENDS", depends)

    image = d.getVar('INITRAMFS_IMAGE', True)
    if image:
        d.appendVarFlag('do_bundle_initramfs', 'depends', ' ${INITRAMFS_IMAGE}:do_rootfs')

    # NOTE: setting INITRAMFS_TASK is for backward compatibility
    #       The preferred method is to set INITRAMFS_IMAGE, because
    #       this INITRAMFS_TASK has circular dependency problems
    #       if the initramfs requires kernel modules
    image_task = d.getVar('INITRAMFS_TASK', True)
    if image_task:
        d.appendVarFlag('do_configure', 'depends', ' ${INITRAMFS_TASK}')
}

inherit kernel-arch deploy

PACKAGES_DYNAMIC += "^kernel-module-.*"
PACKAGES_DYNAMIC += "^kernel-image-.*"
PACKAGES_DYNAMIC += "^kernel-firmware-.*"

export OS = "${TARGET_OS}"
export CROSS_COMPILE = "${TARGET_PREFIX}"

KERNEL_PRIORITY ?= "${@int(d.getVar('PV',1).split('-')[0].split('+')[0].split('.')[0]) * 10000 + \
                       int(d.getVar('PV',1).split('-')[0].split('+')[0].split('.')[1]) * 100 + \
                       int(d.getVar('PV',1).split('-')[0].split('+')[0].split('.')[-1])}"

KERNEL_RELEASE ?= "${KERNEL_VERSION}"

# Where built kernel lies in the kernel tree
KERNEL_OUTPUT ?= "arch/${ARCH}/boot/${KERNEL_IMAGETYPE}"
KERNEL_IMAGEDEST = "boot"

#
# configuration
#
export CMDLINE_CONSOLE = "console=${@d.getVar("KERNEL_CONSOLE",1) or "ttyS0"}"

KERNEL_VERSION = "${@get_kernelversion('${B}')}"

KERNEL_LOCALVERSION ?= ""

# kernels are generally machine specific
PACKAGE_ARCH = "${MACHINE_ARCH}"

# U-Boot support
UBOOT_ENTRYPOINT ?= "20008000"
UBOOT_LOADADDRESS ?= "${UBOOT_ENTRYPOINT}"

# Some Linux kernel configurations need additional parameters on the command line
KERNEL_EXTRA_ARGS ?= ""

# For the kernel, we don't want the '-e MAKEFLAGS=' in EXTRA_OEMAKE.
# We don't want to override kernel Makefile variables from the environment
EXTRA_OEMAKE = ""

KERNEL_ALT_IMAGETYPE ??= ""

# Define where the kernel headers are installed on the target as well as where
# they are staged.
KERNEL_SRC_PATH = "/usr/src/kernel"

KERNEL_IMAGETYPE_FOR_MAKE = "${@(lambda s: s[:-3] if s[-3:] == ".gz" else s)(d.getVar('KERNEL_IMAGETYPE', True))}"

copy_initramfs() {
	echo "Copying initramfs into ./usr ..."
	# In case the directory is not created yet from the first pass compile:
	mkdir -p ${B}/usr
	# Find and use the first initramfs image archive type we find
	rm -f ${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.cpio
	for img in cpio.gz cpio.lz4 cpio.lzo cpio.lzma cpio.xz; do
		if [ -e "${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE}-${MACHINE}.$img" ]; then
			cp ${DEPLOY_DIR_IMAGE}/${INITRAMFS_IMAGE}-${MACHINE}.$img ${B}/usr/.
			case $img in
			*gz)
				echo "gzip decompressing image"
				gunzip -f ${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.$img
				break
				;;
			*lz4)
				echo "lz4 decompressing image"
				lz4 -df ${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.$img
				break
				;;
			*lzo)
				echo "lzo decompressing image"
				lzop -df ${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.$img
				break
				;;
			*lzma)
				echo "lzma decompressing image"
				lzma -df ${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.$img
				break
				;;
			*xz)
				echo "xz decompressing image"
				xz -df ${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.$img
				break
				;;
			esac
		fi
	done
	echo "Finished copy of initramfs into ./usr"
}

INITRAMFS_BASE_NAME = "${KERNEL_IMAGETYPE}-initramfs-${PV}-${PR}-${MACHINE}-${DATETIME}"
INITRAMFS_BASE_NAME[vardepsexclude] = "DATETIME"
do_bundle_initramfs () {
	if [ ! -z "${INITRAMFS_IMAGE}" -a x"${INITRAMFS_IMAGE_BUNDLE}" = x1 ]; then
		echo "Creating a kernel image with a bundled initramfs..."
		copy_initramfs
		if [ -e ${KERNEL_OUTPUT} ] ; then
			mv -f ${KERNEL_OUTPUT} ${KERNEL_OUTPUT}.bak
		fi
		use_alternate_initrd=CONFIG_INITRAMFS_SOURCE=${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.cpio
		kernel_do_compile
		mv -f ${KERNEL_OUTPUT} ${KERNEL_OUTPUT}.initramfs
		mv -f ${KERNEL_OUTPUT}.bak ${KERNEL_OUTPUT}
		# Update install area
		echo "There is kernel image bundled with initramfs: ${B}/${KERNEL_OUTPUT}.initramfs"
		install -m 0644 ${B}/${KERNEL_OUTPUT}.initramfs ${D}/boot/${KERNEL_IMAGETYPE}-initramfs-${MACHINE}.bin
		echo "${B}/${KERNEL_OUTPUT}.initramfs"
	fi
}

python do_devshell_prepend () {
    os.environ["LDFLAGS"] = ''
}

addtask bundle_initramfs after do_install before do_deploy

kernel_do_compile() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
	# The $use_alternate_initrd is only set from
	# do_bundle_initramfs() This variable is specifically for the
	# case where we are making a second pass at the kernel
	# compilation and we want to force the kernel build to use a
	# different initramfs image.  The way to do that in the kernel
	# is to specify:
	# make ...args... CONFIG_INITRAMFS_SOURCE=some_other_initramfs.cpio
	if [ "$use_alternate_initrd" = "" ] && [ "${INITRAMFS_TASK}" != "" ] ; then
		# The old style way of copying an prebuilt image and building it
		# is turned on via INTIRAMFS_TASK != ""
		copy_initramfs
		use_alternate_initrd=CONFIG_INITRAMFS_SOURCE=${B}/usr/${INITRAMFS_IMAGE}-${MACHINE}.cpio
	fi
	oe_runmake ${KERNEL_IMAGETYPE_FOR_MAKE} ${KERNEL_ALT_IMAGETYPE} CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS} $use_alternate_initrd
	if test "${KERNEL_IMAGETYPE_FOR_MAKE}.gz" = "${KERNEL_IMAGETYPE}"; then
		gzip -9c < "${KERNEL_IMAGETYPE_FOR_MAKE}" > "${KERNEL_OUTPUT}"
	fi
}

do_compile_kernelmodules() {
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
	if (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
		oe_runmake ${PARALLEL_MAKE} modules CC="${KERNEL_CC}" LD="${KERNEL_LD}" ${KERNEL_EXTRA_ARGS}
	else
		bbnote "no modules to compile"
	fi
}
addtask compile_kernelmodules after do_compile before do_strip

kernel_do_install() {
	#
	# First install the modules
	#
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
	if (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
		oe_runmake DEPMOD=echo INSTALL_MOD_PATH="${D}" modules_install
		rm "${D}/lib/modules/${KERNEL_VERSION}/build"
		rm "${D}/lib/modules/${KERNEL_VERSION}/source"
		# If the kernel/ directory is empty remove it to prevent QA issues
		rmdir --ignore-fail-on-non-empty "${D}/lib/modules/${KERNEL_VERSION}/kernel"
	else
		bbnote "no modules to install"
	fi

	#
	# Install various kernel output (zImage, map file, config, module support files)
	#
	install -d ${D}/${KERNEL_IMAGEDEST}
	install -d ${D}/boot
	install -m 0644 ${KERNEL_OUTPUT} ${D}/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
	install -m 0644 System.map ${D}/boot/System.map-${KERNEL_VERSION}
	install -m 0644 .config ${D}/boot/config-${KERNEL_VERSION}
	install -m 0644 vmlinux ${D}/boot/vmlinux-${KERNEL_VERSION}
	[ -e Module.symvers ] && install -m 0644 Module.symvers ${D}/boot/Module.symvers-${KERNEL_VERSION}
	install -d ${D}${sysconfdir}/modules-load.d
	install -d ${D}${sysconfdir}/modprobe.d

	#
	# Support for external module building - create a minimal copy of the
	# kernel source tree.
	#
	kerneldir=${D}${KERNEL_SRC_PATH}
	install -d $kerneldir
	mkdir -p ${D}/lib/modules/${KERNEL_VERSION}
	ln -sf ${KERNEL_SRC_PATH} "${D}/lib/modules/${KERNEL_VERSION}/build"

	#
	# Store the kernel version in sysroots for module-base.bbclass
	#

	echo "${KERNEL_VERSION}" > $kerneldir/kernel-abiversion

	#
	# Store kernel image name to allow use during image generation
	#

	echo "${KERNEL_IMAGE_BASE_NAME}" >$kerneldir/kernel-image-name

	#
	# Copy the entire source tree. In case an external build directory is
	# used, copy the build directory over first, then copy over the source
	# dir. This ensures the original Makefiles are used and not the
	# redirecting Makefiles in the build directory.
	#
	find . -depth -not -name "*.cmd" -not -name "*.o" -not -name "*.so.dbg" -not -name "*.so" -not -path "./Documentation*" -not -path "./source*" -not -path "./.*" -print0 | cpio --null -pdlu $kerneldir
	cp .config $kerneldir
	if [ "${S}" != "${B}" ]; then
		pwd="$PWD"
		cd "${S}"
		find . -depth -not -path "./Documentation*" -not -path "./.*" -print0 | cpio --null -pdlu $kerneldir
		cd "$pwd"
	fi

	# Test to ensure that the output file and image type are not actually
	# the same file. If hardlinking is used, they will be the same, and there's
	# no need to install.
	! [ ${KERNEL_OUTPUT} -ef $kerneldir/${KERNEL_IMAGETYPE} ] && install -m 0644 ${KERNEL_OUTPUT} $kerneldir/${KERNEL_IMAGETYPE}
	install -m 0644 System.map $kerneldir/System.map-${KERNEL_VERSION}

	# Dummy Makefile so the clean below works
        mkdir $kerneldir/Documentation
        touch $kerneldir/Documentation/Makefile

	#
	# Clean and remove files not needed for building modules.
	# Some distributions go through a lot more trouble to strip out
	# unecessary headers, for now, we just prune the obvious bits.
	#
	# We don't want to leave host-arch binaries in /sysroots, so
	# we clean the scripts dir while leaving the generated config
	# and include files.
	#
	oe_runmake -C $kerneldir CC="${KERNEL_CC}" LD="${KERNEL_LD}" clean _mrproper_scripts 

	# hide directories that shouldn't have their .c, s and S files deleted
	for d in tools scripts lib; do
		mv $kerneldir/$d $kerneldir/.$d
	done

	# delete .c, .s and .S files, unless we hid a directory as .<dir>. This technique is 
	# much faster than find -prune and -exec
	find $kerneldir -not -path '*/\.*' -type f -name "*.[csS]" -delete

	# put the hidden dirs back
	for d in tools scripts lib; do
		mv $kerneldir/.$d $kerneldir/$d
	done

	# As of Linux kernel version 3.0.1, the clean target removes
	# arch/powerpc/lib/crtsavres.o which is present in
	# KBUILD_LDFLAGS_MODULE, making it required to build external modules.
	if [ ${ARCH} = "powerpc" ]; then
		cp -l arch/powerpc/lib/crtsavres.o $kerneldir/arch/powerpc/lib/crtsavres.o
	fi

	# Necessary for building modules like compat-wireless.
	if [ -f include/generated/bounds.h ]; then
		cp -l include/generated/bounds.h $kerneldir/include/generated/bounds.h
	fi
	if [ -d arch/${ARCH}/include/generated ]; then
		mkdir -p $kerneldir/arch/${ARCH}/include/generated/
		cp -flR arch/${ARCH}/include/generated/* $kerneldir/arch/${ARCH}/include/generated/
	fi

	# Remove the following binaries which cause strip or arch QA errors
	# during do_package for cross-compiled platforms
	bin_files="arch/powerpc/boot/addnote arch/powerpc/boot/hack-coff \
	           arch/powerpc/boot/mktree scripts/kconfig/zconf.tab.o \
		   scripts/kconfig/conf.o scripts/kconfig/kxgettext.o"
	for entry in $bin_files; do
		rm -f $kerneldir/$entry
	done

	# kernels <2.6.30 don't have $kerneldir/tools directory so we check if it exists before calling sed
	if [ -f $kerneldir/tools/perf/Makefile ]; then
		# Fix SLANG_INC for slang.h
		sed -i 's#-I/usr/include/slang#-I=/usr/include/slang#g' $kerneldir/tools/perf/Makefile
	fi
}
do_install[prefuncs] += "package_get_auto_pr"

python sysroot_stage_all () {
    oe.path.copyhardlinktree(d.expand("${D}${KERNEL_SRC_PATH}"), d.expand("${SYSROOT_DESTDIR}${KERNEL_SRC_PATH}"))
}

KERNEL_CONFIG_COMMAND ?= "oe_runmake_call oldnoconfig || yes '' | oe_runmake oldconfig"

kernel_do_configure() {
	# fixes extra + in /lib/modules/2.6.37+
	# $ scripts/setlocalversion . => +
	# $ make kernelversion => 2.6.37
	# $ make kernelrelease => 2.6.37+
	touch ${B}/.scmversion ${S}/.scmversion

	# Copy defconfig to .config if .config does not exist. This allows
	# recipes to manage the .config themselves in do_configure_prepend().
	if [ -f "${WORKDIR}/defconfig" ] && [ ! -f "${B}/.config" ]; then
		cp "${WORKDIR}/defconfig" "${B}/.config"
	fi
	eval ${KERNEL_CONFIG_COMMAND}
}

do_savedefconfig() {
	oe_runmake savedefconfig
}
do_savedefconfig[nostamp] = "1"
addtask savedefconfig after do_configure

inherit cml1

EXPORT_FUNCTIONS do_compile do_install do_configure

# kernel-base becomes kernel-${KERNEL_VERSION}
# kernel-image becomes kernel-image-${KERNEL_VERISON}
PACKAGES = "kernel kernel-base kernel-vmlinux kernel-image kernel-dev kernel-modules"
FILES_${PN} = ""
FILES_kernel-base = "/lib/modules/${KERNEL_VERSION}/modules.order /lib/modules/${KERNEL_VERSION}/modules.builtin"
FILES_kernel-image = "/boot/${KERNEL_IMAGETYPE}*"
FILES_kernel-dev = "/boot/System.map* /boot/Module.symvers* /boot/config* ${KERNEL_SRC_PATH} /lib/modules/${KERNEL_VERSION}/build"
FILES_kernel-vmlinux = "/boot/vmlinux*"
FILES_kernel-modules = ""
RDEPENDS_kernel = "kernel-base"
# Allow machines to override this dependency if kernel image files are 
# not wanted in images as standard
RDEPENDS_kernel-base ?= "kernel-image"
PKG_kernel-image = "kernel-image-${@legitimize_package_name('${KERNEL_VERSION}')}"
PKG_kernel-base = "kernel-${@legitimize_package_name('${KERNEL_VERSION}')}"
RPROVIDES_kernel-base += "kernel-${KERNEL_VERSION}"
ALLOW_EMPTY_kernel = "1"
ALLOW_EMPTY_kernel-base = "1"
ALLOW_EMPTY_kernel-image = "1"
ALLOW_EMPTY_kernel-modules = "1"
DESCRIPTION_kernel-modules = "Kernel modules meta package"

pkg_postinst_kernel-base () {
	if [ ! -e "$D/lib/modules/${KERNEL_VERSION}" ]; then
		mkdir -p $D/lib/modules/${KERNEL_VERSION}
	fi
	if [ -n "$D" ]; then
		depmodwrapper -a -b $D ${KERNEL_VERSION}
	else
		depmod -a ${KERNEL_VERSION}
	fi
}

pkg_postinst_kernel-image () {
	update-alternatives --install /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE} ${KERNEL_IMAGETYPE} /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} ${KERNEL_PRIORITY} || true
}

pkg_postrm_kernel-image () {
	update-alternatives --remove ${KERNEL_IMAGETYPE} ${KERNEL_IMAGETYPE}-${KERNEL_VERSION} || true
}

PACKAGESPLITFUNCS_prepend = "split_kernel_packages "

python split_kernel_packages () {
    do_split_packages(d, root='/lib/firmware', file_regex='^(.*)\.(bin|fw|cis|dsp)$', output_pattern='kernel-firmware-%s', description='Firmware for %s', recursive=True, extra_depends='')
}

do_strip() {
	if [ -n "${KERNEL_IMAGE_STRIP_EXTRA_SECTIONS}" ]; then
		if [ "${KERNEL_IMAGETYPE}" != "vmlinux" ]; then
			bbwarn "image type will not be stripped (not supported): ${KERNEL_IMAGETYPE}"
			return
		fi

		cd ${B}
		headers=`"$CROSS_COMPILE"readelf -S ${KERNEL_OUTPUT} | \
			  grep "^ \{1,\}\[[0-9 ]\{1,\}\] [^ ]" | \
			  sed "s/^ \{1,\}\[[0-9 ]\{1,\}\] //" | \
			  gawk '{print $1}'`

		for str in ${KERNEL_IMAGE_STRIP_EXTRA_SECTIONS}; do {
			if [ "$headers" != *"$str"* ]; then
				bbwarn "Section not found: $str";
			fi

			"$CROSS_COMPILE"strip -s -R $str ${KERNEL_OUTPUT}
		}; done

		bbnote "KERNEL_IMAGE_STRIP_EXTRA_SECTIONS is set, stripping sections:" \
			"${KERNEL_IMAGE_STRIP_EXTRA_SECTIONS}"
	fi;
}
do_strip[dirs] = "${B}"

addtask do_strip before do_sizecheck after do_kernel_link_vmlinux

# Support checking the kernel size since some kernels need to reside in partitions
# with a fixed length or there is a limit in transferring the kernel to memory
do_sizecheck() {
	if [ ! -z "${KERNEL_IMAGE_MAXSIZE}" ]; then
		invalid=`echo ${KERNEL_IMAGE_MAXSIZE} | sed 's/[0-9]//g'`
		if [ -n "$invalid" ]; then
			die "Invalid KERNEL_IMAGE_MAXSIZE: ${KERNEL_IMAGE_MAXSIZE}, should be an integerx (The unit is Kbytes)"
		fi
		size=`du -ks ${B}/${KERNEL_OUTPUT} | awk '{ print $1}'`
		if [ $size -ge ${KERNEL_IMAGE_MAXSIZE} ]; then
			die "This kernel (size=$size(K) > ${KERNEL_IMAGE_MAXSIZE}(K)) is too big for your device. Please reduce the size of the kernel by making more of it modular."
		fi
	fi
}
do_sizecheck[dirs] = "${B}"

addtask sizecheck before do_install after do_strip

KERNEL_IMAGE_BASE_NAME ?= "${KERNEL_IMAGETYPE}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
# Don't include the DATETIME variable in the sstate package signatures
KERNEL_IMAGE_BASE_NAME[vardepsexclude] = "DATETIME"
KERNEL_IMAGE_SYMLINK_NAME ?= "${KERNEL_IMAGETYPE}-${MACHINE}"
MODULE_IMAGE_BASE_NAME ?= "modules-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${DATETIME}"
MODULE_IMAGE_BASE_NAME[vardepsexclude] = "DATETIME"
MODULE_TARBALL_BASE_NAME ?= "${MODULE_IMAGE_BASE_NAME}.tgz"
# Don't include the DATETIME variable in the sstate package signatures
MODULE_TARBALL_SYMLINK_NAME ?= "modules-${MACHINE}.tgz"
MODULE_TARBALL_DEPLOY ?= "1"

do_uboot_mkimage() {
	if test "x${KERNEL_IMAGETYPE}" = "xuImage" ; then 
		if test "x${KEEPUIMAGE}" != "xyes" ; then
			ENTRYPOINT=${UBOOT_ENTRYPOINT}
			if test -n "${UBOOT_ENTRYSYMBOL}"; then
				ENTRYPOINT=`${HOST_PREFIX}nm ${S}/vmlinux | \
					awk '$3=="${UBOOT_ENTRYSYMBOL}" {print $1}'`
			fi
			if test -e arch/${ARCH}/boot/compressed/vmlinux ; then
				${OBJCOPY} -O binary -R .note -R .comment -S arch/${ARCH}/boot/compressed/vmlinux linux.bin
				uboot-mkimage -A ${UBOOT_ARCH} -O linux -T kernel -C none -a ${UBOOT_LOADADDRESS} -e $ENTRYPOINT -n "${DISTRO_NAME}/${PV}/${MACHINE}" -d linux.bin arch/${ARCH}/boot/uImage
				rm -f linux.bin
			else
				${OBJCOPY} -O binary -R .note -R .comment -S vmlinux linux.bin
				rm -f linux.bin.gz
				gzip -9 linux.bin
				uboot-mkimage -A ${UBOOT_ARCH} -O linux -T kernel -C gzip -a ${UBOOT_LOADADDRESS} -e $ENTRYPOINT -n "${DISTRO_NAME}/${PV}/${MACHINE}" -d linux.bin.gz arch/${ARCH}/boot/uImage
				rm -f linux.bin.gz
			fi
		fi
	fi
}

addtask uboot_mkimage before do_install after do_compile

kernel_do_deploy() {
	install -m 0644 ${KERNEL_OUTPUT} ${DEPLOYDIR}/${KERNEL_IMAGE_BASE_NAME}.bin
	if [ ${MODULE_TARBALL_DEPLOY} = "1" ] && (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
		mkdir -p ${D}/lib
		tar -cvzf ${DEPLOYDIR}/${MODULE_TARBALL_BASE_NAME} -C ${D} lib
		ln -sf ${MODULE_TARBALL_BASE_NAME} ${DEPLOYDIR}/${MODULE_TARBALL_SYMLINK_NAME}
	fi

	ln -sf ${KERNEL_IMAGE_BASE_NAME}.bin ${DEPLOYDIR}/${KERNEL_IMAGE_SYMLINK_NAME}.bin
	ln -sf ${KERNEL_IMAGE_BASE_NAME}.bin ${DEPLOYDIR}/${KERNEL_IMAGETYPE}

	cp ${COREBASE}/meta/files/deploydir_readme.txt ${DEPLOYDIR}/README_-_DO_NOT_DELETE_FILES_IN_THIS_DIRECTORY.txt

	cd ${B}
	# Update deploy directory
	if [ -e "${KERNEL_OUTPUT}.initramfs" ]; then
		echo "Copying deploy kernel-initramfs image and setting up links..."
		initramfs_base_name=${INITRAMFS_BASE_NAME}
		initramfs_symlink_name=${KERNEL_IMAGETYPE}-initramfs-${MACHINE}
		install -m 0644 ${KERNEL_OUTPUT}.initramfs ${DEPLOYDIR}/${initramfs_base_name}.bin
		cd ${DEPLOYDIR}
		ln -sf ${initramfs_base_name}.bin ${initramfs_symlink_name}.bin
	fi
}
do_deploy[dirs] = "${DEPLOYDIR} ${B}"
do_deploy[prefuncs] += "package_get_auto_pr"

addtask deploy before do_build after do_install

EXPORT_FUNCTIONS do_deploy

