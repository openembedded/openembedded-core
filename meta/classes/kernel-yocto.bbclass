S = "${WORKDIR}/linux"

do_patch() {
	cd ${S}
	if [ -f ${WORKDIR}/defconfig ]; then
	    defconfig=${WORKDIR}/defconfig
	fi

	kbranch=${KBRANCH}
	if [ -n "${YOCTO_KERNEL_EXTERNAL_BRANCH}" ]; then
           # switch from a generic to a specific branch
           kbranch=${YOCTO_KERNEL_EXTERNAL_BRANCH}
	fi

	# simply ensures that a branch of the right name has been created
	if [ -n "${YOCTO_KERNEL_META_DATA}" ]; then
		createme_flags="--disable-meta-gen"
	fi
	createme ${createme_flags} ${ARCH} ${kbranch} ${defconfig}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not create ${kbranch}"
		exit 1
	fi

	# updates or generates the target description
	if [ -n "${KERNEL_FEATURES}" ]; then
		for feat in ${KERNEL_FEATURES}; do
			addon_features="$addon_features --feature $feat"
		done
	fi
	updateme --branch ${kbranch} -DKDESC=${KMACHINE}:${LINUX_KERNEL_TYPE} \
                         ${addon_features} ${ARCH} ${KMACHINE} ${WORKDIR}
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
	if [ -d ${WORKDIR}/git/.git/refs/remotes/origin ]; then
		echo "Fixing up git directory for ${LINUX_KERNEL_TYPE}/${KMACHINE}"
		rm -rf ${S}
		mkdir ${S}
		mv ${WORKDIR}/git/.git ${S}
	
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

	set +e
	git show-ref --quiet --verify -- "refs/heads/${KBRANCH}"
	if [ $? -eq 0 ]; then
		# checkout and clobber and unimportant files
		git checkout -f ${KBRANCH}
	else
		echo "Not checking out ${KBRANCH}, it will be created later"
		git checkout -f master
	fi
}
do_kernel_checkout[dirs] = "${S}"

addtask kernel_checkout before do_patch after do_unpack

do_kernel_configme[dirs] = "${CCACHE_DIR} ${S} ${B}"
do_kernel_configme() {
	echo "[INFO] doing kernel configme"

	cd ${S}
	configme --reconfig --output ${B} ${KBRANCH} ${KMACHINE}
	if [ $? -ne 0 ]; then
		echo "ERROR. Could not configure ${KMACHINE}-${LINUX_KERNEL_TYPE}"
		exit 1
	fi
	
	echo "# Global settings from linux recipe" >> ${B}/.config
	echo "CONFIG_LOCALVERSION="\"${LINUX_VERSION_EXTENSION}\" >> ${B}/.config
}

python do_kernel_configcheck() {
    import bb, re, string, sys, commands

    bb.plain("NOTE: validating kernel configuration")

    pathprefix = "export PATH=%s; " % bb.data.getVar('PATH', d, True)
    cmd = bb.data.expand("cd ${B}/..; kconf_check -${LINUX_KERNEL_TYPE}-config-${LINUX_VERSION} ${B} ${S} ${B} ${KBRANCH}",d )
    ret, result = commands.getstatusoutput("%s%s" % (pathprefix, cmd))

    bb.plain( "%s" % result )
}


# Ensure that the branches (BSP and meta) are on the locatios specified by
# their SRCREV values. If they are NOT on the right commits, the branches
# are reset to the correct commit.
do_validate_branches() {
	cd ${S}

	# nothing to do if bootstrapping
 	if [ -n "${YOCTO_KERNEL_EXTERNAL_BRANCH}" ]; then
 	 	return
 	fi

	# nothing to do if SRCREV is AUTOREV
	if [ "${SRCREV_machine}" = "AUTOINC" ]; then
		# restore the branch for builds
		git checkout -f ${KBRANCH}
		return
	fi

 	branch_head=`git show-ref -s --heads ${KBRANCH}`
 	meta_head=`git show-ref -s --heads ${KMETA}`
 	target_branch_head="${SRCREV_machine}"
 	target_meta_head="${SRCREV_meta}"

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


