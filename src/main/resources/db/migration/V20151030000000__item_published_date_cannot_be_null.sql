--Copying creation date into published date
UPDATE item SET ite_published = ite_created WHERE ite_published IS NULL;