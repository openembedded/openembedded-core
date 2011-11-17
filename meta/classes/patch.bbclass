# Copyright (C) 2006  OpenedHand LTD

# Point to an empty file so any user's custom settings don't break things
QUILTRCFILE ?= "${STAGING_BINDIR_NATIVE}/quiltrc"

PATCHDEPENDENCY = "${PATCHTOOL}-native:do_populate_sysroot"

python patch_do_patch() {
	import oe.patch

	src_uri = (bb.data.getVar('SRC_URI', d, 1) or '').split()
	if not src_uri:
		return

	patchsetmap = {
		"patch": oe.patch.PatchTree,
		"quilt": oe.patch.QuiltTree,
		"git": oe.patch.GitApplyTree,
	}

	cls = patchsetmap[bb.data.getVar('PATCHTOOL', d, 1) or 'quilt']

	resolvermap = {
		"noop": oe.patch.NOOPResolver,
		"user": oe.patch.UserResolver,
	}

	rcls = resolvermap[bb.data.getVar('PATCHRESOLVE', d, 1) or 'user']

	s = bb.data.getVar('S', d, 1)

	path = os.getenv('PATH')
	os.putenv('PATH', bb.data.getVar('PATH', d, 1))

	classes = {}

	workdir = bb.data.getVar('WORKDIR', d, 1)
	for url in src_uri:
		(type, host, path, user, pswd, parm) = bb.decodeurl(url)

		local = None
		base, ext = os.path.splitext(os.path.basename(path))
		if ext in ('.gz', '.bz2', '.Z'):
			local = os.path.join(workdir, base)
			ext = os.path.splitext(base)[1]

		if "apply" in parm:
			apply = parm["apply"]
			if apply != "yes":
				if apply != "no":
					bb.msg.warn(None, "Unsupported value '%s' for 'apply' url param in '%s', please use 'yes' or 'no'" % (apply, url))
				continue
		#elif "patch" in parm:
			#bb.msg.warn(None, "Deprecated usage of 'patch' url param in '%s', please use 'apply={yes,no}'" % url)
		elif ext not in (".diff", ".patch"):
			continue

		if not local:
			url = bb.encodeurl((type, host, path, user, pswd, []))
			local = os.path.join('/', bb.fetch2.localpath(url, d))
		local = bb.data.expand(local, d)

		if "striplevel" in parm:
			striplevel = parm["striplevel"]
		elif "pnum" in parm:
			#bb.msg.warn(None, "Deprecated usage of 'pnum' url parameter in '%s', please use 'striplevel'" % url)
			striplevel = parm["pnum"]
		else:
			striplevel = '1'

		if "pname" in parm:
			pname = parm["pname"]
		else:
			pname = os.path.basename(local)

		if "mindate" in parm or "maxdate" in parm:
			pn = bb.data.getVar('PN', d, 1)
			srcdate = bb.data.getVar('SRCDATE_%s' % pn, d, 1)
			if not srcdate:
				srcdate = bb.data.getVar('SRCDATE', d, 1)

			if srcdate == "now":
				srcdate = bb.data.getVar('DATE', d, 1)

			if "maxdate" in parm and parm["maxdate"] < srcdate:
				bb.note("Patch '%s' is outdated" % pname)
				continue

			if "mindate" in parm and parm["mindate"] > srcdate:
				bb.note("Patch '%s' is predated" % pname)
				continue


		if "minrev" in parm:
			srcrev = bb.data.getVar('SRCREV', d, 1)
			if srcrev and srcrev < parm["minrev"]:
				bb.note("Patch '%s' applies to later revisions" % pname)
				continue

		if "maxrev" in parm:
			srcrev = bb.data.getVar('SRCREV', d, 1)		
			if srcrev and srcrev > parm["maxrev"]:
				bb.note("Patch '%s' applies to earlier revisions" % pname)
				continue

		if "rev" in parm:
			srcrev = bb.data.getVar('SRCREV', d, 1)		
			if srcrev and parm["rev"] not in srcrev:
				bb.note("Patch '%s' doesn't apply to revision" % pname)
				continue

		if "notrev" in parm:
			srcrev = bb.data.getVar('SRCREV', d, 1)		
			if srcrev and parm["notrev"] in srcrev:
				bb.note("Patch '%s' doesn't apply to revision" % pname)
				continue

		if "patchdir" in parm:
			patchdir = parm["patchdir"]
			if not os.path.isabs(patchdir):
				patchdir = os.path.join(s, patchdir)
		else:
			patchdir = s

		if not patchdir in classes:
			patchset = cls(patchdir, d)
			resolver = rcls(patchset)
			classes[patchdir] = (patchset, resolver)
			patchset.Clean()
		else:
			patchset, resolver = classes[patchdir]

		bb.note("Applying patch '%s' (%s)" % (pname, oe.path.format_display(local, d)))
		try:
			patchset.Import({"file":local, "remote":url, "strippath": striplevel}, True)
		except Exception:
			import sys
			raise bb.build.FuncFailed(str(sys.exc_value))
		resolver.Resolve()
}
patch_do_patch[vardepsexclude] = "DATE SRCDATE PATCHRESOLVE"

addtask patch after do_unpack
do_patch[dirs] = "${WORKDIR}"
do_patch[depends] = "${PATCHDEPENDENCY}"

EXPORT_FUNCTIONS do_patch
