METADATA_BRANCH ?= "${@base_detect_branch(d)}"
METADATA_REVISION ?= "${@base_detect_revision(d)}"

def base_detect_revision(d):
	path = base_get_scmbasepath(d)

	scms = [base_get_metadata_git_revision, \
			base_get_metadata_svn_revision]

	for scm in scms:
		rev = scm(path, d)
		if rev <> "<unknown>":
			return rev

	return "<unknown>"	

def base_detect_branch(d):
	path = base_get_scmbasepath(d)

	scms = [base_get_metadata_git_branch]

	for scm in scms:
		rev = scm(path, d)
		if rev <> "<unknown>":
			return rev.strip()

	return "<unknown>"	

def base_get_scmbasepath(d):
	return d.getVar( 'COREBASE', True)

def base_get_metadata_monotone_branch(path, d):
	monotone_branch = "<unknown>"
	try:
		monotone_branch = file( "%s/_MTN/options" % path ).read().strip()
		if monotone_branch.startswith( "database" ):
			monotone_branch_words = monotone_branch.split()
			monotone_branch = monotone_branch_words[ monotone_branch_words.index( "branch" )+1][1:-1]
	except:
		pass
	return monotone_branch

def base_get_metadata_monotone_revision(path, d):
	monotone_revision = "<unknown>"
	try:
		monotone_revision = file( "%s/_MTN/revision" % path ).read().strip()
		if monotone_revision.startswith( "format_version" ):
			monotone_revision_words = monotone_revision.split()
			monotone_revision = monotone_revision_words[ monotone_revision_words.index( "old_revision" )+1][1:-1]
	except IOError:
		pass
	return monotone_revision

def base_get_metadata_svn_revision(path, d):
	revision = "<unknown>"
	try:
		revision = file( "%s/.svn/entries" % path ).readlines()[3].strip()
	except IOError:
		pass
	return revision

def base_get_metadata_git_branch(path, d):
	branch = os.popen('cd %s; git branch 2>&1 | grep "^* " | tr -d "* "' % path).read()

	if len(branch) != 0:
		return branch
	return "<unknown>"

def base_get_metadata_git_revision(path, d):
	f = os.popen("cd %s; git log -n 1 --pretty=oneline -- 2>&1" % path)
	data = f.read()
	if f.close() is None:        
		rev = data.split(" ")[0]
		if len(rev) != 0:
			return rev
	return "<unknown>"

