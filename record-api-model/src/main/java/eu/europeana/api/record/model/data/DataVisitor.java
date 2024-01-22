package eu.europeana.api.record.model.data;

public interface DataVisitor
{
    public void visit(DatatypeLiteral literal);
}
