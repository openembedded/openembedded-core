S = "${WORKDIR}/linux"

# Determine which branch to fetch and build. Not all branches are in the
# upstream repo (but will be locally created after the fetchers run) so 
# a fallback branch needs to be chosen. 
#
# The default machine 'UNDEFINED'. If the machine is not set to a specific
# branch in this recipe or in a recipe extension, then we fallback to a 
# branch that is always present 'standard'. This sets the KBRANCH variable
# and is used in the SRC_URI. The machine is then set back to ${MACHINE},
# since futher processing will use that to create local branches
python __anonymous () {
    import bb, re, string

    version = bb.data.getVar("LINUX_VERSION", d, 1)
    # 2.6.34 signifies the old-style tree, so we need some temporary
    # conditional processing based on the kernel version.
    if version == "2.6.34":
        bb.data.setVar("KBRANCH", "${KMACHINE}-${LINUX_KERNEL_TYPE}", d)
        bb.data.setVar("KMETA", "wrs_meta", d)
        mach = bb.data.getVar("KMACHINE", d, 1)
        if mach == "UNDEFINED":
            bb.data.setVar("KBRANCH", "standard", d)
            bb.data.setVar("KMACHINE", "${MACHINE}", d)
            # track the global configuration on a bootstrapped BSP
            bb.data.setVar("SRCREV_machine", "${SRCREV_meta}", d)
            bb.data.setVar("BOOTSTRAP", bb.data.expand("${MACHINE}",d) + "-standard", d)
    else:
        # The branch for a build is:
        #    yocto/<kernel type>/${KMACHINE} or
        #    yocto/<kernel type>/${KMACHINE}/base
        mach = bb.data.getVar("KMACHINE_" + bb.data.expand("${MACHINE}",d), d, 1)
        if mach == None:
             mach = bb.data.getVar("KMACHINE", d, 1)

        bb.data.setVar("KBRANCH", mach, d)
        bb.data.setVar("KMETA", "meta", d)

        # drop the "/base" if it was on the KMACHINE
        kmachine = mach.replace('/base','')
        # drop everything but the last segment
        kmachine = os.path.basename( kmachine )
        # and then write KMACHINE back
        bb.data.setVar('KMACHINE_' + bb.data.expand("${MACHINE}",d), kmachine, d)

        if mach == "UNDEFINED":
            bb.data.setVar('KMACHINE_' + bb.data.expand("${MACHINE}",d), bb.data.expand("${MACHINE}",d), d)
            bb.data.setVar("KBRANCH", "yocto/standard/base", d)
            bb.data.setVar("SRCREV_machine", "standard", d)
            bb.data.setVar("BOOTSTRAP", "yocto/standard/" + bb.data.expand("${MACHINE}",d), d)
}

do_patch() {
	cd ${S}
	if [ -f ${WORKDIR}/defconfig ]; then
	    defconfig=${WORKDIR}/defconfig
	fi

	kbranch=${KBRANCH}
	if [ -n "${BOOTSTRAP}" ]; then
           # switch from a generic to a specific branch
           kbranch=${BOOTSTRAP}
	fi


	# simply ensures that a branch of the right name has been created
	createme ${ARCH} ${kbranch} ${defconfig}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not create ${kbranch}"
		exit 1
	fi

	# updates or generates the target description
	if [ -n "${KERNEL_FEATURES}" ]; then
	       addon_features="--features ${KERNEL_FEATURES}"
	fi
	updateme ${addon_features} ${ARCH} ${WORKDIR}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not update ${kbranch}"
		exit 1
	fi

	# executes and modifies the source tree as required
	patchme ${kbranch}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not modify ${kbranch}"
		exit 1
	fi
}

do_kernel_checkout() {
	if [ -d ${WORKDIR}/.git/refs/remotes/origin ]; then
		echo "Fixing up git directory for ${LINUX_KERNEL_TYPE}/${KMACHINE}"
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
				b=`echo $r | cut -d' ' -f2 | sed 's%refs/remotes/origin/%%'`;
				dir=`dirname $b`
				mkdir -p .git/refs/heads/$dir
				echo $ref > .git/refs/heads/$b
			done
			cd ..
		else
			cp -r ${S}/.git/refs/remotes/origin/* ${S}/.git/refs/heads
			rmdir ${S}/.git/refs/remotes/origin
		fi
	fi
	cd ${S}

	# checkout and clobber and unimportant files
	git checkout -f ${KBRANCH}
}
do_kernel_checkout[dirs] = "${S}"

addtask kernel_checkout before do_patch after do_unpack

do_kernel_configme() {
	echo "[INFO] doing kernel configme"

	cd ${S}
	configme --reconfig --output ${B}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not configure ${KMACHINE}-${LINUX_KERNEL_TYPE}"
		exit 1
	fi

	echo "# Global settings from linux recipe" >> ${B}/.config
	echo "CONFIG_LOCALVERSION="\"${LINUX_VERSION_EXTENSION}\" >> ${B}/.config
}

do_kernel_configcheck() {
	echo "[INFO] validating kernel configuration"
	cd ${B}/..
	kconf_check ${B}/.config ${B} ${S} ${B} ${LINUX_VERSION} ${KMACHINE}-${LINUX_KERNEL_TYPE}
}


# Ensure that the branches (BSP and meta) are on the locatios specified by
# their SRCREV values. If they are NOT on the right commits, the branches
# are reset to the correct commit.
do_validate_branches() {
	cd ${S}
 	branch_head=`git show-ref -s --heads ${KBRANCH}`
 	meta_head=`git show-ref -s --heads ${KMETA}`
 	target_branch_head="${SRCREV_machine}"
 	target_meta_head="${SRCREV_meta}"

	# nothing to do if bootstrapping
 	if [ -n "${BOOTSTRAP}" ]; then
 	 	return
 	fi

	current=`git branch |grep \*|sed 's/^\* //'`
	if [ -n "$target_branch_head" ] && [ "$branch_head" != "$target_branch_head" ]; then
		if [ -n "${KERNEL_REVISION_CHECKING}" ]; then
			ref=`git show ${target_meta_head} 2>&1 | head -n1 || true`
			if [ "$ref" = "fatal: bad object ${target_meta_head}" ]; then
				echo "ERROR ${target_branch_head} is not a valid commit ID."
				echo "The kernel source tree may be out of sync"
				exit 1
			else
				echo "Forcing branch $current to ${target_branch_head}"
				git branch -m $current $current-orig
				git checkout -b $current ${target_branch_head}
			fi
		fi
	fi

	if [ "$meta_head" != "$target_meta_head" ]; then
		if [ -n "${KERNEL_REVISION_CHECKING}" ]; then
			ref=`git show ${target_meta_head} 2>&1 | head -n1 || true`
			if [ "$ref" = "fatal: bad object ${target_meta_head}" ]; then
				echo "ERROR ${target_meta_head} is not a valid commit ID"
				echo "The kernel source tree may be out of sync"
				exit 1
			else
				echo "Forcing branch meta to ${target_meta_head}"
				git branch -m ${KMETA} ${KMETA}-orig
				git checkout -b ${KMETA} ${target_meta_head}
			fi	   
		fi
	fi

	# restore the branch for builds
	git checkout -f ${KBRANCH}
}

# Many scripts want to look in arch/$arch/boot for the bootable
# image. This poses a problem for vmlinux based booting. This 
# task arranges to have vmlinux appear in the normalized directory
# location.
do_kernel_link_vmlinux() {
	if [ ! -d "${B}/arch/${ARCH}/boot" ]; then
		mkdir ${B}/arch/${ARCH}/boot
	fi
	cd ${B}/arch/${ARCH}/boot
	ln -sf ../../../vmlinux
}


