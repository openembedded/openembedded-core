DESCRIPTION = "Wind River Kernel"
SECTION = "kernel"
LICENSE = "GPL"

# Set this to 'preempt_rt' in the local.conf if you want a real time kernel
LINUX_KERNEL_TYPE ?= standard
SRCREV_FORMAT = "meta_machine"
PV = "2.6.34+git${SRCPV}"

# To use a staged, on-disk bare clone of a Wind River Kernel, use a 
# variant of the below
# SRC_URI = "git://///path/to/kernel/default_kernel.git;fullclone=1"
SRC_URI = "git://git.pokylinux.org/linux-2.6-windriver.git;protocol=git;fullclone=1;branch=${WRMACHINE}-${LINUX_KERNEL_TYPE};name=machine \
           git://git.pokylinux.org/linux-2.6-windriver.git;protocol=git;noclone=1;branch=wrs_meta;name=meta"

SRC_URI_append_atom-pc = "\
           file://i915.cfg \
           file://8250.cfg"
SRC_URI_append_qemux86 = " file://x86-wrs.cfg"
SRC_URI_append_qemux86-64 = " file://x86-wrs.cfg"
           
WRMACHINE = "${MACHINE}"
WRMACHINE_qemux86  = "common_pc"
WRMACHINE_qemux86-64  = "common_pc_64"
WRMACHINE_qemuppc  = "qemu_ppc32"
WRMACHINE_qemumips = "mti_malta32_be"
WRMACHINE_qemuarm  = "arm_versatile_926ejs"
WRMACHINE_atom-pc  = "common_pc"

COMPATIBLE_MACHINE = "(qemuarm|qemux86|qemuppc|qemumips|qemux86-64|atom-pc)"

LINUX_VERSION = "v2.6.34"
LINUX_VERSION_EXTENSION = "-wr-${LINUX_KERNEL_TYPE}"
PR = "r7"

S = "${WORKDIR}/linux"
B = "${WORKDIR}/linux-${WRMACHINE}-${LINUX_KERNEL_TYPE}-build"

# functionality flags
force_revisions = "t"

do_patch() {
	cd ${S}
	if [ -f ${WORKDIR}/defconfig ]; then
	    defconfig=${WORKDIR}/defconfig
	fi

	# simply ensures that a branch of the right name has been created
	createme ${ARCH} ${WRMACHINE}-${LINUX_KERNEL_TYPE} ${defconfig}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not create ${WRMACHINE}-${LINUX_KERNEL_TYPE}"
		exit 1
	fi

	# updates or generates the target description
	updateme ${ARCH} ${WORKDIR}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not update ${WRMACHINE}-${LINUX_KERNEL_TYPE}"
		exit 1
	fi

	# executes and modifies the source tree as required
	patchme ${WRMACHINE}-${LINUX_KERNEL_TYPE}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not modify ${WRMACHINE}-${LINUX_KERNEL_TYPE}"
		exit 1
	fi
}

validate_branches() {
	branch_head=`git show-ref -s --heads ${WRMACHINE}-${LINUX_KERNEL_TYPE}`
	meta_head=`git show-ref -s --heads wrs_meta`
	target_branch_head="${SRCREV_machine}"
	target_meta_head="${SRCREV_meta}"

	if [ -n "$target_branch_head" ] && [ "$branch_head" != "$target_branch_head" ]; then
		if [ -n "${force_revisions}" ]; then
			git show ${target_branch_head} > /dev/null 2>&1
			if [ $? -eq 0 ]; then
				echo "Forcing branch ${WRMACHINE}-${LINUX_KERNEL_TYPE} to ${target_branch_head}"
				git branch -m ${WRMACHINE}-${LINUX_KERNEL_TYPE} ${WRMACHINE}-${LINUX_KERNEL_TYPE}-orig
				git checkout -b ${WRMACHINE}-${LINUX_KERNEL_TYPE} ${target_branch_head}
			else
				echo "ERROR ${target_branch_head} is not a valid commit ID."
				echo "The kernel source tree may be out of sync"
				exit 1
			fi	       
		fi
	fi

	if [ "$meta_head" != "$target_meta_head" ]; then
		if [ -n "${force_revisions}" ]; then
			git show ${target_meta_head} > /dev/null 2>&1
			if [ $? -eq 0 ]; then
				echo "Forcing branch wrs_meta to ${target_meta_head}"
				git branch -m wrs_meta wrs_meta-orig
				git checkout -b wrs_meta ${target_meta_head}
			else
				echo "ERROR ${target_meta_head} is not a valid commit ID"
				echo "The kernel source tree may be out of sync"
				exit 1
			fi	   
		fi
	fi
}

do_wrlinux_checkout() {
	if [ -d ${WORKDIR}/.git/refs/remotes/origin ]; then
		echo "Fixing up git directory for ${WRMACHINE}-${LINUX_KERNEL_TYPE}"
		rm -rf ${S}
		mkdir ${S}
		mv ${WORKDIR}/.git ${S}
	
		if [ -e ${S}/.git/packed-refs ]; then
			cd ${S}
			rm -f .git/refs/remotes/origin/HEAD
IFS='
';

			for r in `git show-ref | grep remotes`; do
				ref=`echo $r | cut -d' ' -f1`; 
				b=`echo $r | cut -d'/' -f4`;
				echo $ref > .git/refs/heads/$b
			done
			cd ..
		else
			mv ${S}/.git/refs/remotes/origin/* ${S}/.git/refs/heads
			rmdir ${S}/.git/refs/remotes/origin
		fi
	fi
	cd ${S}

	# checkout and clobber and unimportant files
	git checkout -f ${WRMACHINE}-${LINUX_KERNEL_TYPE}

	validate_branches

	# this second checkout is intentional, we want to leave ourselves
	# on the branch to be built, but validate_branches could have changed
	# our initial checkout. So we do it a second time to be sure
	git checkout -f ${WRMACHINE}-${LINUX_KERNEL_TYPE}
}
do_wrlinux_checkout[dirs] = "${S}"

addtask wrlinux_checkout before do_patch after do_unpack

do_wrlinux_configme() {
	echo "Doing wrlinux configme"

	cd ${S}
	configme --reconfig
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not configure ${WRMACHINE}-${LINUX_KERNEL_TYPE}"
		exit 1
	fi

	echo "# CONFIG_WRNOTE is not set" >> ${B}/.config
	echo "# Global settings from linux recipe" >> ${B}/.config
	echo "CONFIG_LOCALVERSION="\"${LINUX_VERSION_EXTENSION}\" >> ${B}/.config
}

do_wrlinux_configcheck() {
	echo "[INFO] validating kernel configuration"
	cd ${B}/..
	kconf_check ${B}/.config ${B} ${S} ${B} ${LINUX_VERSION} ${WRMACHINE}-${LINUX_KERNEL_TYPE}
}

do_wrlinux_link_vmlinux() {
	cd ${B}/arch/${ARCH}/boot
	ln -sf ../../../vmlinux
}

do_compile_perf() {
	oe_runmake -C ${S}/tools/perf CC="${KERNEL_CC}" LD="${KERNEL_LD}" prefix=${prefix}
}

do_install_perf() {
	oe_runmake -C ${S}/tools/perf CC="${KERNEL_CC}" LD="${KERNEL_LD}" prefix=${prefix} DESTDIR=${D} install
}

do_patch[depends] = "kern-tools-native:do_populate_sysroot"
addtask wrlinux_configme before do_configure after do_patch
addtask wrlinux_link_vmlinux after do_compile before do_install
addtask wrlinux_configcheck after do_configure before do_compile

inherit kernel

# perf tasks
addtask compile_perf after do_compile before do_install
addtask install_perf after do_install before do_package do_deploy

do_compile_perf[depends] =  "virtual/libc:do_populate_sysroot"
do_compile_perf[depends] =+ "elfutils:do_populate_sysroot"
RDEPENDS_perf += "python perl elfutils"
