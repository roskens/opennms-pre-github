CREATE SEQUENCE asset_cats_seq;
DROP TABLE asset_cats CASCADE;
CREATE TABLE asset_cats (
	catID		integer CONSTRAINT pk_catID PRIMARY KEY DEFAULT nextval('asset_cats_seq'),
	category 	varchar(64) NOT NULL DEFAULT 'new category' );

DROP TABLE manufaturers CASCADE;
CREATE TABLE manufacturers (
	manID		integer CONSTRAINT pk_manID PRIMARY KEY,
	manufacturer	varchar(64) NOT NULL DEFAULT 'new manufacturer');

DROP TABLE vendors CASCADE;
CREATE TABLE vendors (
	vendorID	integer CONSTRAINT pk_vendorID PRIMARY KEY,
	vendor		varchar(64) NOT NULL DEFAULT 'new vendor',
	vendorPhone	varchar(64),
	vendorFax	varchar(64),
	vendorAssetNumber varchar(64) );

DROP TABLE operating_systems CASCADE;
CREATE TABLE operating_systems (
	osID		integer	CONSTRAINT pk_osID PRIMARY KEY,
	os		varchar(64) NOT NULL DEFAULT 'new os');

DROP TABLE assets CASCADE;
CREATE TABLE assets (
	nodeID		integer CONSTRAINT pk_assets_nodedID PRIMARY KEY CONSTRAINT fk_nodeID5 REFEREnCES node (nodeID) ON DELETE CASCADE,
	catID		integer CONSTRAINT fk_catID1 references asset_cats (catID),
	manID		integer CONSTRAINT fk_manID1 references manufacturers (manID),
	vendorID	integer CONSTRAINT fk_vendorID1 references vendors (vendorID),
	osID		integer CONSTRAINT fk_osID1 references operating_systems (osID),
	modelNumber	varchar(64),
	serialNumber	varchar(64),
	description	varchar(128),
	circuitId	varchar(64),
	assetNumber	varchar(64),
	rack		varchar(64),
	slot		varchar(64),
	port		varchar(64),
	region		varchar(64),
	division	varchar(64),
	department      varchar(64),
	address1	varchar(256),
	address2	varchar(256),
	city		varchar(64),
	state		varchar(64),
	zip		varchar(64),
	building	varchar(64),
	floor		varchar(64),
	room		varchar(64),
	userLastModified char(20) not null DEFAULT 'unknown',
	lastModifiedDate timestamp not null DEFAULT current_timestamp,
	dateInstalled	varchar(64),
	lease		varchar(64),
	leaseExpires    varchar(64),
	supportPhone    varchar(64),
	maintContract   varchar(64),
	maintContractExpires varchar(64),
	displayCategory	varchar(64),
	notifyCategory	varchar(64),
	pollerCategory	varchar(64),
	thresholdCategory varchar(64),
	comment		varchar(1024)
       );
create index assets_catID_idx ON assets (catID);
create index assets_manID_idx ON assets (manID);
create index assets_vendorID_idx ON assets (vendorID);
create index assets_osID_idx ON assets (osID);

